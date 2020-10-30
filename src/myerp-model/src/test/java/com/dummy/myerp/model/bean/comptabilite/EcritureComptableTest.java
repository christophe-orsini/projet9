package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

import java.math.BigDecimal;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

/*
 * RG_Compta_2
 * Pour qu'une écriture comptable soit valide, elle doit être équilibrée :
 * la somme des montants au crédit des lignes d'écriture doit être égale à la somme des montants au débit
 *
 * RG_Compta_3
 * Une écriture comptable doit contenir au moins deux lignes d'écriture : une au débit et une au crédit
 *
 * RG_Compta_5
 * La référence d'une écriture comptable est composée du code du journal dans lequel figure l'écriture suivi de l'année et
 * d'un numéro de séquence (propre à chaque journal) sur 5 chiffres incrémenté automatiquement à chaque écriture
 * Le formatage de la référence est : XX-AAAA/#####
 * Ex : Journal de banque (BQ), écriture au 31/12/2016--> BQ-2016/00001
 *
 * RG_Compta_6
 * La référence d'une écriture comptable doit être unique, il n'est pas possible de créer plusieurs écritures ayant la même référence
 */

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
	void getTotalDebit_sommeDeuxLignes_retourneTotalDebit()
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
    void getTotalDebit_NeContientPasAuMoinsDeuxLignes()
    void getTotalDebit_AvecUneSeuleLigne()
    {
    	// arrange
		ecritureUnderTest.setLibelle("Débit");
    	ecritureUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.55", null));
    	
    	// act
    	assertThatIllegalStateException().isThrownBy(() -> {ecritureUnderTest.getTotalDebit();}).
    		withMessage("Une écriture comptable doit avoir au moins 2 lignes d'ecriture");
    }
	
	@Test
	void getTotalCredit_sommeDeuxLignes_retourneTotalCredit()
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
    public void isEquilibree_doitEtreEquilibree()
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
    public void isEquilibree_neDoitPasEtreEquilibree()
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
