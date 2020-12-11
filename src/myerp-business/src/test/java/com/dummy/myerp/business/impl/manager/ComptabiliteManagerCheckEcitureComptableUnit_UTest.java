package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ComptabiliteManagerCheckEcitureComptableUnit_UTest
{
    private ComptabiliteManagerImpl managerUnderTest;

    @BeforeEach
	void setUp() throws Exception
	{
    	managerUnderTest = new ComptabiliteManagerImpl();
	}
	
	@AfterEach
	void CleanUp()
	{
		managerUnderTest = null;
	}
	
    @Test
    public void checkEcritureComptableUnit_WithoutReference_ShouldNotRaiseException() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable = Mockito.spy(new EcritureComptable());
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
        		new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),null,
        		null, new BigDecimal(123)));
        
        // act
        managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);
        
        // assert
        Mockito.verify(vEcritureComptable).getReference(); // Fin de la méthode checkEcritureComptableUnit atteinte
    }
    
    @Test
    public void checkEcritureComptableUnit_WithReference_ShouldNotRaiseException() throws Exception
	{
    	// arrange
		EcritureComptable vEcritureComptable = Mockito.spy(new EcritureComptable());
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		Calendar calendar = Calendar.getInstance();
		String annee = String.valueOf(calendar.get(Calendar.YEAR));
		vEcritureComptable.setReference("AC-" + annee + "/00001");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),null, null, new BigDecimal(123)));
	  
		// act
		managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);
		
		// assert
		Mockito.verify(vEcritureComptable).getJournal(); // Fin de la méthode checkEcritureComptableUnit atteinte
	}

    @Test
    public void checkEcritureComptableUnit_ConstraintViolation() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
                
        // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);})
        	.withCauseExactlyInstanceOf(ConstraintViolationException.class)
        	.withMessage("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @Test
    public void checkEcritureComptableUnit_IsNotEquilibree_RG2() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
        		new BigDecimal(123), null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2), null,
        		null, new BigDecimal(1234)));
        
        // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);})
        	.withMessage("L'écriture comptable n'est pas équilibrée.");
    }

    @Test
    public void checkEcritureComptableUnit_Only1Line_RG3() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),null,
        		new BigDecimal(123),new BigDecimal(123)));
        
        // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);})
        	.withMessage("L'écriture comptable ne respecte pas les règles de gestion.");
    }
    
    @Test
    public void checkEcritureComptableUnit_OnlyDebit_RG3() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),null,
        		new BigDecimal(123),null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
        		new BigDecimal(456), null));
        
        // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);})
        	.withMessage("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
    }
    
    @Test
    public void checkEcritureComptableUnit_OnlyCredit_RG3() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
        		null, new BigDecimal(123)));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
                null, new BigDecimal(456)));
        
        // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);})
        	.withMessage("L'écriture comptable doit avoir au moins deux lignes : une ligne au débit et une ligne au crédit.");
    }
    
	@Test
    public void checkEcritureComptableUnit_AnneReference_IsNot_AnneEcriture()
    {
    	// arrange
    	 EcritureComptable vEcritureComptable;
         vEcritureComptable = new EcritureComptable();
         vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
         vEcritureComptable.setDate(new Date());
         vEcritureComptable.setLibelle("Libelle");
         vEcritureComptable.setReference("AC-2016/00001");
         vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
         		null, new BigDecimal(123)));
         vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
                 new BigDecimal(123), null));
    	
    	// assert
    	assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);})
    		.withMessage("L'année de la référence ne correspond pas à la date de l'écriture comptable.");
    }
	
	@Test
    public void checkEcritureComptableUnit_JournalReference_IsNot_JournalEcriture()
    {
    	// arrange
    	 EcritureComptable vEcritureComptable;
         vEcritureComptable = new EcritureComptable();
         vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
         vEcritureComptable.setDate(new Date());
         vEcritureComptable.setLibelle("Libelle");
         Calendar calendar = Calendar.getInstance();
	     calendar.setTime(vEcritureComptable.getDate());
	     String annee = String.valueOf(calendar.get(Calendar.YEAR));
         vEcritureComptable.setReference("BQ-" + annee + "/00001");
         vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
         		null, new BigDecimal(123)));
         vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null,
                 new BigDecimal(123), null));
    	
    	// assert
    	assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);})
    		.withMessage("Le code du journal de la référence ne correspond pas au journal l'écriture comptable.");
    }
}
