package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.InfantVisitRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.InfantVisitRecordValidator;

public class AddPostpartumCareAction {
    private InfantVisitRecordDAO infantVisitRecordDAO;
    private long loggedInMID;

    /**
     * AddPostpartumCareAction Constructor
     *
     * @param factory
     * @param loggedInMID
     */
    public AddPostpartumCareAction(DAOFactory factory, long loggedInMID) {
        this.infantVisitRecordDAO = factory.getInfantVisitRecordDAO();
        this.loggedInMID = loggedInMID;
    }

    /**
     * Add a new postpartum care record
     *
     * @param p InfantVisitRecordBean
     *          throws FormValidationException if the infant is not successfully validated
     *          throws ITrustException
     */

    public long addInfantVisitRecord(InfantVisitRecordBean p) throws FormValidationException, ITrustException {
        if (p != null) {
            new InfantVisitRecordValidator().validate(p);
            infantVisitRecordDAO.addInfantVisitRecord(p);
            long mid = p.getMid();
            TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_INFANT_CARE_RECORD, loggedInMID, mid, "Postpartum care visit created for patient with MID " + String.valueOf(mid));
            return mid;
        }
        return -1;
    }
}







