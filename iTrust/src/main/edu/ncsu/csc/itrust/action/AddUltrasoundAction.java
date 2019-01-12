package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.UltrasoundRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.UltrasoundRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.UltrasoundRecordValidator;

import java.util.Date;

public class AddUltrasoundAction {
    private UltrasoundRecordDAO ultrasoundRecordDAO;
    private long loggedInMID;
    /**
     *  AddObstetricsAction Constructor
     * @param factory
     * @param loggedInMID
     */
    public AddUltrasoundAction(DAOFactory factory, long loggedInMID) {
        this.ultrasoundRecordDAO = factory.getUltrasoundRecordDAO();
        this.loggedInMID = loggedInMID;
    }

    /**
     * Add a new ultrasound record
     * @param p UltrasoundRecordBean
     * throws FormValidationException if the patient is not successfully validated
     * throws ITrustException
     */

    public long addUltrasoundRecord(UltrasoundRecordBean p) throws FormValidationException, ITrustException {
    	if (p != null) {
            new UltrasoundRecordValidator().validate(p);
            ultrasoundRecordDAO.addUltrasoundRecord(p);
            long mid = p.getMid();
            long fetusID = p.getFetusID();
            TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_INITIAL_ULTRASOUND_RECORD, loggedInMID, mid, "Ultrasound record created for " + String.valueOf(mid) + ", baby #" + String.valueOf(fetusID));
            System.out.println(mid);
            return mid;
        }
    	return -1;
    }
}

