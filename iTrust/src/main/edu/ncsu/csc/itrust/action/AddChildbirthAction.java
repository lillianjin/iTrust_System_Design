package edu.ncsu.csc.itrust.action;

import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.ChildbirthRecordDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.model.old.validate.ChildbirthRecordValidator;

public class AddChildbirthAction {
	private ChildbirthRecordDAO childbirthRecordDAO;
    private long loggedInMID;
    /**
     *  AddChildbirthAction Constructor
     * @param factory
     * @param loggedInMID
     */
    public AddChildbirthAction(DAOFactory factory, long loggedInMID) {
        this.childbirthRecordDAO = factory.getChildbirthRecordDAO();
        this.loggedInMID = loggedInMID;
    }
    
    /**
     * Add a new childbirth record
     * @param p ChildbirthRecordBean
     * throws FormValidationException if the patient is not successfully validated
     * throws ITrustException
     */
    public long addChildbirthRecord(ChildbirthRecordBean p) throws FormValidationException, ITrustException {
        if (p != null) {
    		new ChildbirthRecordValidator().validate(p);
            childbirthRecordDAO.addChildbirthRecord(p);
            long mid = p.getMid();
            long babyID = p.getBabyID();
            TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_INITIAL_CHILDBIRTH_RECORD, loggedInMID, mid, "Childbirth visit created for " + String.valueOf(mid) + ", baby #" + String.valueOf(babyID));      
            System.out.println(p.getMid());
            return p.getMid();
        }
    	return -1;
    }
}
