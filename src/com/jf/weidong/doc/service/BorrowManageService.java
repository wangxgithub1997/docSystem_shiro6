package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.*;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.*;
import com.jf.weidong.doc.domain.query.Query;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.domain.vo.BorrowInfoListVO;
import com.jf.weidong.doc.mapper.BorrowManageMapper;
import com.jf.weidong.doc.utils.JDBCTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BorrowManageService {
    //借阅dao
    @Autowired
    BorrowManageDao borrowManageDao ;//= new BorrowManageDao();
    //读者管理
    @Autowired
    ReaderManagerDao readerManagerDao ;//= new ReaderManagerDao();
    //图书管理
    @Autowired
    BookManageDao bookManageDao ;//= new BookManageDao();

    //读者类型
    @Autowired
    ReaderTypeDao readerTypeDao ;//= new ReaderTypeDao();

    //逾期
    @Autowired
    OverDueInfoDao overDueInfoDao ;//= new OverDueInfoDao();

    //归还
    @Autowired
    BackManageDao backManageDao ;//= new BackManageDao();

    @Autowired
    BorrowManageMapper borrowManageMapper;
    public PageBean<BorrowInfoListVO> pageSearch(Query query) {

        PageBean<BorrowInfoListVO> pb=new PageBean<>();
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        List<BorrowInfoListVO> borrowInfoListVOS = borrowManageMapper.pageSearch(query);
        pb.setList(borrowInfoListVOS);
        Integer totalCount = borrowManageMapper.totalCount();
        pb.setTotalCount(totalCount);
        pb.setPageSize(query.getPageSize());
        return pb;
    }

    /*
     * 1. 根据页面传过来的数据，查询借阅的读者信息
     * 2. 判断读者证件号、以及密码是和页面传过来的数据匹配
     *      匹配：继续执行
     *      不匹配：提示【密码错误】
     * 3. 查询图书的ISBN是否存在
     *      存在：继续执行
     *      不存在：返回图书ISBN错误
     * 4. 获取该读者的借阅信息
     * 	  查询该读者类型得出读者的最大借阅数量，匹配借阅的数量是否小于最大借阅量
     * 	    小于：可以借阅
     * 		大于：不可以借阅，直接返回借阅数量已超过
     * 5. 查看读者的罚款信息，是否有未缴纳的罚款
     * 		有：返回有尚未缴纳的罚金，缴纳罚金后再借书
     * 	    没有：继续执行
     * 6. 查询借阅的书籍，查看该书籍的在馆数量是否大于1
     * 	    大于1：继续
     * 		小于等于1：提示该图书为馆内最后一本，无法借阅
     * 7. 处理借阅信息
     * 	  得到该读者的最大借阅天数，和每日罚金
     *    获得当前时间，根据最大借阅天数，计算出截止日期
     * 	  设置每日的罚金金额和截止日期
     * 	  设置借阅状态和逾期天数
     * 	  获得该读者的信息，为借阅信息设置读者信息
     * 	  获得图书信息，为借阅信息设置图书信息
     * 	  获得管理员信息，为借阅信息设置操作的管理员信息
     * 8. 存储借阅信息
     * 9. 借阅的书籍的在馆数量需要减少
     * 10. 设置归还信息
     */
    public int addborrow(ReaderDO readerDO, String ISBN, AdminDetailsVO adminDetailsVO) {
        try {
            // 1. 根据页面传过来的数据，查询借阅的读者信息
            ReaderDO readerDetailsVO = readerManagerDao.getReaderDOByPaperNO(readerDO);
            if (readerDetailsVO == null) {
                return 2;//读者证件号错误（读者不存在）
            }

            //判断读者证件号、以及密码是和页面传过来的数据匹配
            //匹配：继续执行
            //不匹配：提示【密码错误】
            if (!readerDO.getPassword().equals(readerDetailsVO.getPassword())) {
                return -1;
            }

            //查询图书的ISBN是否存在
            BookDO bookByISBN = bookManageDao.getBookByISBN(new BookDO(ISBN));
            if (bookByISBN == null) {
                return 3;
            }

            //查询该读者类型得出读者的最大借阅数量，匹配借阅的数量是否小于最大借阅量
            int count = borrowManageDao.getNoBackNumberByReaderId(readerDetailsVO);
            ReaderTypeDO readerTypeById = readerTypeDao.getReaderTypeById(new ReaderTypeDO(readerDetailsVO.getFk_readerType()));
            if (count >= readerTypeById.getMaxNum()) {
                return -2;
            }

            //查看读者的罚款信息，是否有未缴纳的罚款
            //通过读者id查询罚款信息
            List<OverDueInfoDO> overdueByReaderId = overDueInfoDao.getOverdueByReaderId(readerDetailsVO);
            for (OverDueInfoDO o : overdueByReaderId) {
                if (o.getIsPay() == 0) {
                    return -3;
                }
            }

            //查询借阅的书籍，查看该书籍的在馆数量是否大于1
            if (bookByISBN.getCurrentNum() <= 1) {
                return -4;
            }

            //处理借阅信息

            int maxDay = readerTypeById.getBday();//读者借阅的天数
            Date data = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(data);
            calendar.add(Calendar.DAY_OF_MONTH, maxDay);

            BorrowInfoDO borrowInfoDO = new BorrowInfoDO();

            borrowInfoDO.setBorrowDate(new java.sql.Date(data.getTime()));
            borrowInfoDO.setEndDate(new java.sql.Date(calendar.getTime().getTime()));
            borrowInfoDO.setPenalty(readerTypeById.getPenalty());


            borrowInfoDO.setFk_reader(readerDetailsVO.getId());//借阅的读者
            borrowInfoDO.setFk_admin(adminDetailsVO.getId());//操作的管理员
            borrowInfoDO.setFk_book(bookByISBN.getId());

            //借阅状态
            borrowInfoDO.setState(0);
            borrowInfoDO.setOverday(0);

            System.out.println(borrowInfoDO);

            JDBCTools.start();

            int id = borrowManageDao.addBorrow(borrowInfoDO);
            if (id != -1) {
                //借阅的书籍在馆数量减少
                bookByISBN.setCurrentNum(bookByISBN.getCurrentNum() - 1);
                bookManageDao.updateBookCurrentNum(bookByISBN);

                //设置归还信息
                BackInfoDO backInfoDO = new BackInfoDO();
                backInfoDO.setId(id);
                int addbackCount = backManageDao.addBack(backInfoDO);
                JDBCTools.commit();
                return addbackCount;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                JDBCTools.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCTools.close();
        }
        return 0;
    }


    /**
     * 检查借阅信息，查看是否有逾期记录
     *	1.查询所有的未归还的借阅记录(包括未归还，逾期未归还，续借未归还，续借逾期未归还)
     * 	2.遍历所有的未归还的借阅记录，判断当前时间和借阅的截止的时间的大小
     *	  小于：直接跳过
     *	  大于：则需要进行逾期处理，往下执行
     *	3.更新借阅信息
     *   	计算逾期的天数（用当前时间减去截止时间）
     *   	设置当前的借阅状态
     *          如果是未归还，则改为逾期未归还
     *          如果是续借未归还，则改为续借逾期未归还
     *	4.需要生成罚金记录
     *	  得到当前借阅记录的罚金金额，和当前的逾期天数进行相乘，得到罚金金额，并插入数据库
     */
    public void checkBorrowInfo(){
        try {
            List<BorrowInfoDO> list = borrowManageDao.getNoBackBorrowList();
            if (list != null) {
                for (BorrowInfoDO borrowInfoDO : list) {
                    long endTime = borrowInfoDO.getEndDate().getTime();//截止日期
                    long currentTime = System.currentTimeMillis();
                    if (currentTime > endTime) {
                        //逾期了
                        double days = (currentTime - endTime) / (1000 * 60 * 60 * 24);
                        System.out.println("逾期的天数：" + days);


                        borrowInfoDO.setOverday((int)days);
                        if (borrowInfoDO.getState() == 0) {
                            borrowInfoDO.setState(1);
                        } else if (borrowInfoDO.getState() == 3) {
                            borrowInfoDO.setState(4);
                        }
                        borrowManageDao.updateBorrowStateAndOverdue(borrowInfoDO);
                        OverDueInfoDO overDueInfoDO = new OverDueInfoDO();
                        overDueInfoDO.setId(borrowInfoDO.getId());
                        overDueInfoDO.setForfeit(borrowInfoDO.getPenalty() * days);
                        OverDueInfoDO oldOverdue = overDueInfoDao.getOverDueById(new OverDueInfoDO(borrowInfoDO.getId()));
                        if (oldOverdue == null) {
                            System.out.println("先前没有记录");
                            overDueInfoDao.addOverdueInfo(overDueInfoDO);
                        }else{
                            System.out.println("先前有记录");
                            overDueInfoDao.updateOverdueInfo(overDueInfoDO);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }








}
