package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsRecordDAO;
import edu.ncsu.csc.itrust.model.old.validate.ObstetricsRecordValidator;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;


public class EditObstetricsAction {
    private ObstetricsRecordDAO obstetricsRecordDAO;
    private long loggedInMID;

    /**
     *  EditObstetricsAction Constructor
     * @param factory
     * @param loggedInMID
     */
    public EditObstetricsAction(DAOFactory factory, long loggedInMID) {
        this.obstetricsRecordDAO = factory.getObstetricsRecordDAO();
        this.loggedInMID = loggedInMID;
    }

    /**
     * Edit an existing obstetrics record
     * @param p
     * @throws FormValidationException
     * @throws ITrustException
     */
    public void editObstetricsRecord(ObstetricsRecordBean p) throws FormValidationException, ITrustException {
        if (p != null) {
            new ObstetricsRecordValidator().validate(p);
            obstetricsRecordDAO.editObstetricsRecordOV(p);
            TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_OBSTETRICS_OV, loggedInMID,
                    p.getMid(), "Edit office obstetrics record added on" + p.geinitDateString() + " for oid " + Long.toString(p.getOid()));

        }
    }
}
