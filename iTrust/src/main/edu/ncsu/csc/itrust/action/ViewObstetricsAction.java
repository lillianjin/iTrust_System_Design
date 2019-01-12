package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsRecordDAO;
import edu.ncsu.csc.itrust.exception.ITrustException;

public class ViewObstetricsAction {
    private ObstetricsRecordDAO obstetricsRecordDAO;
    private long loggedInMID;
    private EventLoggingAction loggingAction;

    /**
     * ViewObstetricsAction Constructor
     * @param factory
     * @param loggedInMID
     */
    public ViewObstetricsAction(DAOFactory factory, long loggedInMID) throws ITrustException{
        this.obstetricsRecordDAO = factory.getObstetricsRecordDAO();
        this.loggedInMID = loggedInMID;
        this.loggingAction = new EventLoggingAction(factory);
    }

    /**
     * returns a list of obstetrics record beans for all types of records
     * @param mid
     * @return The list of obstetrics records
     * @throws ITrustException When there is a bad user
     */
    public List<ObstetricsRecordBean> getObstetricsRecordByMID(long mid) throws ITrustException {
        return obstetricsRecordDAO.getObstetricsRecordsByMID(mid);
    }

    /**
     * returns a list of obstetrics record beans for initial obstetrics records
     * @param mid
     * @return The list of obstetrics records
     * @throws ITrustException When there is a bad user
     */
    public List<ObstetricsRecordBean> getInitialObstetricsRecordByMID(long mid) throws ITrustException {
        return obstetricsRecordDAO.getObstetricsRecordsByMIDInitial(mid);
    }

    /**
     * returns a list of obstetrics record beans for prior pregnancy obstetrics records
     * @param mid
     * @return The list of obstetrics records
     * @throws ITrustException When there is a bad user
     */
    public List<ObstetricsRecordBean> getPriorPregnancyObstetricsRecordByMID(long mid) throws ITrustException{
        return obstetricsRecordDAO.getObstetricsRecordsByMIDPrior(mid);
    }

    /**
     * returns a list of obstetrics record beans for office visit obstetrics records
     * @param mid
     * @return The list of obstetrics records
     * @throws ITrustException When there is a bad user
     */
    public List<ObstetricsRecordBean> getOfficeVisitObstetricsRecordByMID(long mid) throws ITrustException {
        return obstetricsRecordDAO.getObstetricsRecordsByMIDOV(mid);
    }
}
