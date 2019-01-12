package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.enums.PregnancyStatus;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ObstetricsRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.ObstetricsRecordValidator;

public class AddObstetricsAction {
    private ObstetricsRecordDAO obstetricsRecordDAO;
    private long loggedInMID;

    /**
     *  AddObstetricsAction Constructor
     * @param factory
     * @param loggedInMID
     */
    public AddObstetricsAction(DAOFactory factory, long loggedInMID) {
        this.obstetricsRecordDAO = factory.getObstetricsRecordDAO();
        this.loggedInMID = loggedInMID;
    }

    /**
     * Add a new obstetrics record
     * @param p ObstetricsRecordBean
     * throws FormValidationException if the patient is not successfully validated
     * throws ITrustException
     */

    public long addObstetricsRecord(ObstetricsRecordBean p) throws FormValidationException, ITrustException {
    	if (p != null) {
    		new ObstetricsRecordValidator().validate(p);
            obstetricsRecordDAO.addObstetricsRecord(p);

            // if it is creating initial record
            if (p.getPregStatus() == PregnancyStatus.Initial) {
                long mid = p.getMid();
                String eddString = p.getEDDString();
                TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_INITIAL_OBSTETRIC_RECORD, loggedInMID, mid, "the estimated due date is " + eddString);
            }

            // else if it is creating office visit record
            else if (p.getPregStatus() == PregnancyStatus.OfficeVisit) {
                long mid = p.getMid();
                TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_OBSTETRICS_OV, loggedInMID, mid, "Office visit created for" + String.valueOf(mid));
            }
            return p.getMid();
        }
    	return -1;
    }
}

