package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class EcritureComptableTest
{
	EcritureComptable ecritureUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		ecritureUnderTest = new EcritureComptable();
	}
	
	@AfterEach
	void CleanUp()
	{
		ecritureUnderTest = null;
	}
	
	@Test
	void getTotalDebit_ShouldReturnTotalDebit()
	{
		// arrange
		ecritureUnderTest.setLibelle("Débit");
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.55", null));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
    	
    	// act
    	BigDecimal actualResult = ecritureUnderTest.getTotalDebit();
    	
    	// assert
    	assertThat(actualResult).isEqualByComparingTo(new BigDecimal("301.05"));
	}
	
	@Test
	void getTotalCredit_ShouldReturnTotalCredit()
	{
		// arrange
		ecritureUnderTest.setLibelle("Débit");
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, null, "19.55"));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "30"));
    	
    	// act
    	BigDecimal actualResult = ecritureUnderTest.getTotalCredit();
    	
    	// assert
    	assertThat(actualResult).isEqualByComparingTo(new BigDecimal("49.55"));
	}
	
    @Test
    public void isEquilibree_ShouldBeEquilibree()
    {
    	// arrange  
    	ecritureUnderTest.setLibelle("Equilibrée");
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "301"));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        
        // act
        boolean actualResult = ecritureUnderTest.isEquilibree();
        
        // assert
        assertThat(actualResult).isTrue();
    }
    
    @Test
    public void isEquilibree_ShouldNotBeEquilibree()
    {
    	// arrange
    	ecritureUnderTest.setLibelle("Non équilibrée");
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "10", null));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "30"));
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        
        // act
        boolean actualResult = ecritureUnderTest.isEquilibree();
        
        // assert
        assertThat(actualResult).isFalse();
    }
    
    //************************* Methods
    private LigneEcritureComptable createLigne(Integer pCompteComptableNumero, String pDebit, String pCredit)
    {
        BigDecimal vDebit = pDebit == null ? null : new BigDecimal(pDebit);
        BigDecimal vCredit = pCredit == null ? null : new BigDecimal(pCredit);
        String vLibelle = ObjectUtils.defaultIfNull(vDebit, BigDecimal.ZERO)
                                     .subtract(ObjectUtils.defaultIfNull(vCredit, BigDecimal.ZERO)).toPlainString();
        LigneEcritureComptable vRetour = new LigneEcritureComptable(new CompteComptable(pCompteComptableNumero),
                                                                    vLibelle,
                                                                    vDebit, vCredit);
        return vRetour;
    }
}
