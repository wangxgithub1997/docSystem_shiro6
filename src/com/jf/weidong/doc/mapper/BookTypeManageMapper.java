package com.jf.weidong.doc.mapper;

import com.jf.weidong.doc.domain.PageBean;
import com.jf.weidong.doc.domain.data.BookTypeDO;
import com.jf.weidong.doc.domain.query.BookTypeQuery;

import java.util.List;

//?为什么是用的list封装
public interface BookTypeManageMapper {
    List<BookTypeDO> pageSearch(BookTypeQuery query);
    Integer totalCount (BookTypeQuery query);
    BookTypeDO getBookTypeById(Integer id);
    Integer upadteBookTypeInfo(BookTypeDO typeDO);
    BookTypeDO getBookTypeById(BookTypeDO BookTypeDO);

    /*根据图书分类名称查找是否有此分类信息*/
    BookTypeDO getBookTypeByName(BookTypeDO typeDO);
    Integer addBookType(BookTypeDO typeDO);

    Integer deleteBookTypeById(BookTypeDO typeDO);
}
