package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.InfantVisitRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.InfantVisitRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.InfantVisitRecordValidator;

public class EditPostpartumCareAction {

    private InfantVisitRecordDAO infantVisitRecordDAO;
    private long loggedInMID;
    /**
     *  EditPostpartumCareAction Constructor
     * @param factory
     * @param loggedInMID
     */
    public EditPostpartumCareAction(DAOFactory factory, long loggedInMID) {
        this.infantVisitRecordDAO = factory.getInfantVisitRecordDAO();
        this.loggedInMID = loggedInMID;
    }

    /**
     * Edit an existing children postpartum care record
     * @param p
     * @throws FormValidationException
     * @throws ITrustException
     */
    public void editInfantVisitRecord(InfantVisitRecordBean p) throws FormValidationException, ITrustException {
        if (p != null) {
            new InfantVisitRecordValidator().validate(p);
            infantVisitRecordDAO.editInfantVisitRecord(p);
            long mid = p.getMid();
            TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_INFANT_CARE_RECORD, loggedInMID,
                    mid, "Edit postpartum care record added on" + p.getVisitDate() + " for patient with MID " + String.valueOf(mid));

        }
    }
}
