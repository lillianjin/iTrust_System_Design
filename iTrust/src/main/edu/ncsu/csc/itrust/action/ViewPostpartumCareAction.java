package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.InfantVisitRecordDAO;

import java.util.List;

public class ViewPostpartumCareAction {
    private InfantVisitRecordDAO ivDAO;
    private long loggedInMID;
    private EventLoggingAction loggingAction;

    public ViewPostpartumCareAction(DAOFactory factory, long loggedInMID) throws ITrustException {
        this.ivDAO = factory.getInfantVisitRecordDAO();
        this.loggedInMID = loggedInMID;
        this.loggingAction = new EventLoggingAction(factory);
    }

    public List<InfantVisitRecordBean> getInfantVisitRecordsByMID(long mid) throws ITrustException {
        return ivDAO.getInfantVisitRecordsByMID(mid);
    }
}
