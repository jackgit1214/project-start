package com.project.core.mybatis.util;

import java.io.Serializable;
import java.util.List;


/**
 * 分页的数据模型类，用于页面展示分页数据
 *
 * @author lilj
 */
public class PageResult<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6405087297327140471L;

    private int DEFAULTPAGEGROUPNUM = 5;

    private int[] pageGroup = new int[DEFAULTPAGEGROUPNUM];
    /**
     * 当前页的记录列表
     */
    private List<T> pageDatas;

    /**
     * 缓冲数据存储上页及下页，是否可提高速度有待验证
     */
    private List<T> nextPageDatas;
    private List<T> priorPageDatas;

    /**
     * 当前页开始行
     */
    private int startRow;

    /**
     * 当前页结束行
     */
    private int endRow;

    /**
     * 页行数
     */
    private int pageSize;

    /**
     * 全部记录的数量
     */
    private int totalSize;

    /**
     * 第一页
     */
    private int firstPage = 1;

    /**
     * 上一页的页码
     */
    private int prevPage;

    /**
     * 下一页的页码
     */
    private int nextPage;

    /**
     * 最后一页的页码，就是总页数
     */
    private int lastPage;

    /**
     * 当前页是第几页
     */
    private int curPage;

    public PageResult() {
    }

    public PageResult(int curPage, int totalSize, int pageSize) {
        this.pageSize = pageSize;
        this.totalSize = totalSize;
        this.caculatePage(curPage);
    }

    public PageResult(int curPage, int pageSize) {
        this.pageSize = pageSize;
        this.curPage = curPage;
        this.caculatePage(curPage);
    }


    public void caculatePage(int curPage) {
        if (totalSize <= 0)
            return;
        if (pageSize <= 0)
            return;
        //总页数，即最后一页的页码，总行数除以每页行数
        if (totalSize % pageSize > 0)
            this.lastPage = totalSize / pageSize + 1;
        else
            this.lastPage = totalSize / pageSize;

        if (this.lastPage == 0)
            this.lastPage = 1;

        if (curPage > this.lastPage)
            this.curPage = this.lastPage;
        else
            this.curPage = curPage;

        //当前页的开始行
        this.startRow = (this.curPage - 1) * pageSize;
        //当前页的结束行
        this.endRow = this.curPage * pageSize;
        if (this.endRow > this.totalSize)
            this.endRow = this.totalSize;

        if (this.curPage == this.firstPage)
            this.prevPage = 0; //为0是表示没有前页
        else
            this.prevPage = this.curPage - 1;

        if (this.curPage == this.lastPage)
            this.nextPage = 0;
        else
            this.nextPage = this.curPage + 1;

        int tmppage = 0;
        if (this.curPage % this.DEFAULTPAGEGROUPNUM == 0) {
            tmppage = this.curPage / this.DEFAULTPAGEGROUPNUM - 1;
        } else
            tmppage = this.curPage / this.DEFAULTPAGEGROUPNUM;

        pageGroup[0] = tmppage * this.DEFAULTPAGEGROUPNUM + 1;
        pageGroup[1] = pageGroup[0] + 1;
        pageGroup[2] = pageGroup[0] + 2;
        pageGroup[3] = pageGroup[0] + 3;
        pageGroup[4] = pageGroup[0] + 4;

    }

    public List<T> getPageDatas() {
        return pageDatas;
    }


    public void setPageDatas(List<T> pageDatas) {
        this.pageDatas = pageDatas;
    }


    public List<T> getNextPageDatas() {
        return nextPageDatas;
    }


    public void setNextPageDatas(List<T> nextPageDatas) {
        this.nextPageDatas = nextPageDatas;
    }


    public List<T> getPriorPageDatas() {
        return priorPageDatas;
    }


    public void setPriorPageDatas(List<T> priorPageDatas) {
        this.priorPageDatas = priorPageDatas;
    }


    public int getStartRow() {
        return startRow;
    }


    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }


    public int getEndRow() {
        return endRow;
    }


    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }


    public int getPageSize() {
        return pageSize;
    }


    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }


    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
        this.caculatePage(this.curPage);
    }


    public int getFirstPage() {
        return firstPage;
    }


    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }


    public int getPrevPage() {
        return prevPage;
    }


    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }


    public int getNextPage() {
        return nextPage;
    }


    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }


    public int getLastPage() {
        return lastPage;
    }


    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }


    public int getCurPage() {
        return curPage;
    }


    public void setCurPage(int curPage) {
        this.curPage = curPage;

    }

    public int[] getPageGroup() {


        return pageGroup;

    }

    @Override
    public String toString() {
        return "Page{" +
                "pageNum=" + curPage +
                ", pageSize=" + pageSize +
                ", startRow=" + startRow +
                ", endRow=" + endRow +
                ", total=" + totalSize +
                ", pages=" + lastPage +
                '}';
    }
}
