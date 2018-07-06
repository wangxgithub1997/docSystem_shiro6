package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.BackManageDao;
import com.jf.weidong.doc.dao.BookManageDao;
import com.jf.weidong.doc.dao.BorrowManageDao;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BackInfoDO;
import com.jf.weidong.doc.domain.data.BookDO;
import com.jf.weidong.doc.domain.data.BorrowInfoDO;
import com.jf.weidong.doc.domain.query.BackListQuery;
import com.jf.weidong.doc.domain.vo.AdminDetailsVO;
import com.jf.weidong.doc.domain.vo.BackListVO;
import com.jf.weidong.doc.mapper.BackManageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class BackManagerService {
    @Autowired
    BackManageDao backManageDao; //= new BackManageDao();
    @Autowired
    BorrowManageDao borrowManageDao;// = new BorrowManageDao();
    @Autowired
    BookManageDao bookManageDao;// = new BookManageDao();

    @Autowired
    BackManageMapper backManageMapper;
    public PageBean<BackListVO> pageSearch(BackListQuery query)  {

        PageBean<BackListVO> pb=new PageBean<>();
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        List<BackListVO> backListVOS = backManageMapper.pageSearch(query);
        pb.setList(backListVOS);
        Integer totalCount = backManageMapper.totalCount(query);
        pb.setTotalCount(totalCount);
        pb.setCurrentPage(query.getPageCode());
        return pb;
    }


    /**
     * 1. 通过借阅id获取该借阅的归还状态，
     *      图书已经归还：提示该书籍已经归还
     *      为归还：继续下一步
     * 2. 获取书籍信息，书籍归还后在馆数量应该增加
     * 3. 设置归还的时间、和管理员数据
     * 4. 设置借阅的状态
     *      如果当前的借阅状态不属于续借，那么设置为归还
     *      如果当前的状态属于续借，设置为续借归还
     * 5. 如果有罚款记录，那么提示去交罚款
     */
    public int addBackInfo(BackInfoDO backInfoDO, AdminDetailsVO adminDetailsVO) {
        try {
            BorrowInfoDO borrowInfoById = borrowManageDao.getBorrowInfoById(new BorrowInfoDO(backInfoDO.getId()));

            if (borrowInfoById.getState() == 2 || borrowInfoById.getState() == 5) {
                //该书已经还了
                return -1;
            }
            //获取书籍信息，书籍归还后在馆数量应该增加

            BookDO bookById = bookManageDao.getBookById(new BookDO(borrowInfoById.getFk_book()));
            bookById.setCurrentNum(bookById.getCurrentNum() + 1);
            bookManageDao.updateBookCurrentNum(bookById);

            BackInfoDO biDO = new BackInfoDO();
            biDO.setId(borrowInfoById.getId());
            biDO.setBackDate(new Date(System.currentTimeMillis()));
            //设置归还的管理员
            biDO.setFk_admin(adminDetailsVO.getId());
            //更新归还的信息
            backManageDao.updateBackInfo(biDO);

            //记录之前的借阅状态
            int state = borrowInfoById.getState();

            //如果当前的借阅状态不属于续借，那么设置为归还
            if (borrowInfoById.getState() == 0 || borrowInfoById.getState() == 1) {
                borrowInfoById.setState(2);
            }

            //如果当前的状态属于续借，设置为续借归还
            if (borrowInfoById.getState() == 3 || borrowInfoById.getState() == 4) {
                borrowInfoById.setState(5);
            }

            //更新借阅的状态
            borrowManageDao.updateBorrowInfo(borrowInfoById);
            //判断之前的状态
            if (state == 1 || state == 4) {
                return 2;
            }else{
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
