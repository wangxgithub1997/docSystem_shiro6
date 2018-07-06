package com.jf.weidong.doc.service;

import com.jf.weidong.doc.dao.*;
import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BorrowInfoDO;
import com.jf.weidong.doc.domain.data.OverDueInfoDO;
import com.jf.weidong.doc.domain.data.ReaderDO;
import com.jf.weidong.doc.domain.data.ReaderTypeDO;
import com.jf.weidong.doc.domain.query.BorrowSearchQuery;
import com.jf.weidong.doc.domain.vo.BorrowSearchListVO;
import com.jf.weidong.doc.domain.vo.ReaderDetailsVO;
import com.jf.weidong.doc.mapper.BorrowSearchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: weidong
 * @create: 2018-06-13 00:28
 **/
@Service
public class BorrowSearchService {
    @Autowired
    private BorrowSearchDao borrowSearchDao ;//= new BorrowSearchDao();
    @Autowired
    BorrowManageDao borrowManageDao ;//= new BorrowManageDao();
    @Autowired
    ReaderManagerDao readerManagerDao ;//= new ReaderManagerDao();
    @Autowired
    ReaderTypeDao readerTypeDao ;//= new ReaderTypeDao();
    @Autowired
    OverDueInfoDao overDueInfoDao ;//= new OverDueInfoDao();
    @Autowired
    BorrowSearchMapper borrowSearchMapper;
    public PageBean<BorrowSearchListVO> pageSearch(BorrowSearchQuery query){

        PageBean<BorrowSearchListVO> pb=new PageBean<>();
        query.setStart((query.getPageCode()-1)*query.getPageSize());
        List<BorrowSearchListVO> list = borrowSearchMapper.pageSearch(query);
        pb.setList(list);
        Integer totalCount = borrowSearchMapper.totalCount(query);
        pb.setTotalCount(totalCount);
        pb.setPageSize(query.getPageSize());
        return pb;
    }



    //续借步骤：
    /* 1. 得到借阅的记录，并获取借阅的记录的状态
     *   如果是续借状态(包括续借未归还，续借逾期未归还)，返回返回-2，显示【已经续借过了】
     * 	 如果是归还状态(包括归还，续借归还)，返回-1，显示【该书已还无法续借】
     *	 以上都不是以上情况，则进行下一步。
     * 2. 获取借阅的读者，得到借阅的读者类型，并通过读者类型得到可续借的天数
     * 3. 计算是否可续借，在当前记录的截止日期上叠加可续借天数
     * 	 如果叠加后的时间比当前时间小，则返回返回-3，显示【你已超续借期了，无法进行续借,提示读者快去还书和缴纳罚金】
     * 	 如果大于当前时间进行下一步
     * 4. 判断当前借阅记录的状态
     * 	 如果当前记录为逾期未归还，则需要取消当前借阅记录的罚金记录，并将该记录的状态设置为续借未归还
     *   如果为未归还状态，直接将当前状态设置为续借未归还
     * 5. 更新借阅信息，返回1，显示【更新成功】
     *
     *
     * 1. 借阅日期   6.1             归还日期  6. 10
     *                              续借：6. 20
     *    书是6.1借的 6.25 再进行续借
     */
    public int renewBook(BorrowInfoDO borrowInfoDO){

        //只能续借一次
        try {
            BorrowInfoDO borrowInfoById = borrowManageDao.getBorrowInfoById(borrowInfoDO);
            Integer state = borrowInfoById.getState();
            if (state == 3 || state == 4) {
                return -2;
            }
            if (state == 2 || state == 5) {
                return -1;
            }
            //获取借阅的读者，得到借阅的读者类型，并通过读者类型得到可续借的天数
            ReaderDetailsVO readerDetails = readerManagerDao.getReaderDetails(new ReaderDO(borrowInfoById.getFk_reader()));
            ReaderTypeDO readerTypeByName = readerTypeDao.getReaderTypeByName(new ReaderTypeDO(readerDetails.getReaderType()));
            Integer renewDays = readerTypeByName.getRenewDays();
            System.out.println("借阅时间："+borrowInfoById.getBorrowDate().toLocaleString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(borrowInfoById.getEndDate());
            calendar.add(Calendar.DAY_OF_MONTH, renewDays);
            //计算出结束的时间了
            Date endData = calendar.getTime();
            System.out.println("续借后的时间："+endData.toLocaleString());
            if (System.currentTimeMillis() > endData.getTime()) {
                return -3;
            }
            //如果当前记录为逾期未归还，则需要取消当前借阅记录的罚金记录，并将该记录的状态设置为续借未归还
            if (state == 1) {
                overDueInfoDao.deleteOverDueInfo(new OverDueInfoDO(borrowInfoById.getId()));
            }
            //将状态设置为续借为归还
            borrowInfoById.setState(3);
            borrowInfoById.setEndDate(new java.sql.Date(endData.getTime()));
            borrowManageDao.updateBorrowStateAndEndDate(borrowInfoById);
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }












}
