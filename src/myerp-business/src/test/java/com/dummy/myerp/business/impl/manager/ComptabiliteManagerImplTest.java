package com.dummy.myerp.business.impl.manager;

import java.math.BigDecimal;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ComptabiliteManagerImplTest
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
    public void checkEcritureComptableUnit() throws Exception {
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(123)));
        managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);
    }

    @Test
    public void checkEcritureComptableUnitViolation() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
                
        // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() ->
        {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);
        }).withMessage("L'écriture comptable ne respecte pas les règles de gestion.");
    }

    @Test
    public void checkEcritureComptableUnitRG2() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),
                                                                                 null, null,
                                                                                 new BigDecimal(1234)));
        // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() ->
        {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);
        }).withMessage("L'écriture comptable n'est pas équilibrée.");
    }

    @Test
    public void checkEcritureComptableUnitRG3() throws Exception
    {
    	// arrange
        EcritureComptable vEcritureComptable;
        vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
        vEcritureComptable.setDate(new Date());
        vEcritureComptable.setLibelle("Libelle");
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
        vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1),
                                                                                 null, new BigDecimal(123),
                                                                                 null));
     // assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() ->
        {
        	managerUnderTest.checkEcritureComptableUnit(vEcritureComptable);
        }).withMessage("L'écriture comptable n'est pas équilibrée.");
    }
}
