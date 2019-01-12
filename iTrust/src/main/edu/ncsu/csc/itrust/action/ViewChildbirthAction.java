package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildbirthRecordDAO;

public class ViewChildbirthAction {
    private ChildbirthRecordDAO crDAO;
    private long loggedInMID;
    private EventLoggingAction loggingAction;

    public ViewChildbirthAction(DAOFactory factory, long loggedInMID) throws ITrustException {
        this.crDAO = factory.getChildbirthRecordDAO();
        this.loggedInMID = loggedInMID;
        this.loggingAction = new EventLoggingAction(factory);
    }

    public List<ChildbirthRecordBean> getChildbirthRecordsByMID(long mid) throws ITrustException {
        return crDAO.getChildbirthRecordsByMID(mid);
    }
}
