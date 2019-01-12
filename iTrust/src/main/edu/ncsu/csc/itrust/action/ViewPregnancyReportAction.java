package edu.ncsu.csc.itrust.action;

import java.util.List;

import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.logger.TransactionLogger;
import edu.ncsu.csc.itrust.model.old.beans.ObstetricsRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.AllergyBean;
import edu.ncsu.csc.itrust.model.old.beans.ChildbirthRecordBean;
import edu.ncsu.csc.itrust.model.old.beans.PatientBean;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.*;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;

public class ViewPregnancyReportAction {
	private AllergyDAO allergyDAO;
	private ChildbirthRecordDAO childbirthRecordDAO;
	private PatientDAO patientDAO;
	private ObstetricsRecordDAO obstetricsRecordDAO;
	long loggedInMID;
	long MID;
	
	 /**
     * The constructor pass in the loggedInMID and MID that needs the report generation.
     *
     * @param factory The {@link DAOFactory} associated with this DAO, which is used
     *                for obtaining SQL connections, etc.
     *        loggedInMID The logged in MID
     *        MID The MID that needs the report.
     */
	
	public ViewPregnancyReportAction(DAOFactory factory, long loggedInMID, long MID) throws ITrustException {
		this.loggedInMID = loggedInMID;
		this.MID = MID;
		allergyDAO = factory.getAllergyDAO();
		childbirthRecordDAO = factory.getChildbirthRecordDAO();
		patientDAO = factory.getPatientDAO();
		obstetricsRecordDAO = factory.getObstetricsRecordDAO();
		TransactionLogger.getInstance().logTransaction(TransactionType.CREATE_PREGNANCY_REPORT, this.loggedInMID, this.MID, "Pregancy report is created for " + String.valueOf(MID));
	}
	
	/**
     * Returns previous obstetrics visit record where pregnancyStatus = 'Complete'.
     *
     * @return List of ObstetricsRecordBean with pregnancyStatus = 'Complete' given MID.
     */
	
	public List<ObstetricsRecordBean> getObstetricsRecordBeanByMIDPrior() throws ITrustException {
		return obstetricsRecordDAO.getObstetricsRecordsByMIDPrior(MID);
	}
	
	/**
     * Returns childbirth visit record.
     *
     * @return List of ChildbirthRecordBean given MID.
     */
	
	public List<ChildbirthRecordBean> getChildbirthReocrdBeanByMID() throws ITrustException {
		return childbirthRecordDAO.getChildbirthRecordsByMID(MID);
	}
	
	/**
     * Returns patient information.
     *
     * @return PatientBean given MID.
     */
	
	public PatientBean getPatientReocrdBeanByMID() throws ITrustException {
		return patientDAO.getPatient(MID);
	}
	
	/**
     * Returns current obstetrics visit record where pregnancyStatus = 'OfficVisit'.
     *
     * @return List of ObstetricsRecordBean with pregnancyStatus = 'OfficeVisit' given MID.
     */
	
	public List<ObstetricsRecordBean> getObstetricsRecordBeanByMIDOV() throws ITrustException {
		return obstetricsRecordDAO.getObstetricsRecordsByMIDOV(MID);
	}
	
	/**
     * Returns patient's allergy information.
     *
     * @return List of AllergyBean given MID.
     */
	
	public List<AllergyBean> getAllergies() throws ITrustException {
		return allergyDAO.getAllergies(MID);
	}
	
}
