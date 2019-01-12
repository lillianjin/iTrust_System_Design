package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildbirthRecordDAO;
import edu.ncsu.csc.itrust.model.old.validate.ChildbirthRecordValidator;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class EditChildbirthAction {
    private ChildbirthRecordDAO crDAO;
    private long loggedInMID;

    public EditChildbirthAction(DAOFactory factory, long loggedInMID) {
        this.crDAO = factory.getChildbirthRecordDAO();
        this.loggedInMID = loggedInMID;
    }

    public void editChildbirthRecord(ChildbirthRecordBean p) throws FormValidationException, ITrustException {
        if (p != null) {
            new ChildbirthRecordValidator().validate(p);
            crDAO.editChildbirthRecordOV(p);
            TransactionLogger.getInstance().logTransaction(TransactionType.EDIT_CHILDBIRTH_VISIT, loggedInMID,
                    p.getMid(), "Edit childbirth record added for oid " + Long.toString(p.getOid()));
        }
    }
}
