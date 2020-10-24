package com.dummy.myerp.model.bean.comptabilite;

import static org.assertj.core.api.Assertions.assertThat;

//import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.MathContext;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class EcritureComptableTest
{
	EcritureComptable ecritueUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
		ecritueUnderTest = new EcritureComptable();
	}
	
	@AfterEach
	void CleanUp()
	{
		ecritueUnderTest = null;
	}
	
	@Test
	void getTotalDebit_sommeDeDeuxLignes()
	{
		// arrange
		ecritueUnderTest.setLibelle("Débit");
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.55", null));
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
    	
    	// act
    	BigDecimal actualResult = ecritueUnderTest.getTotalDebit();
    	
    	// assert
    	assertThat(actualResult).isEqualTo(new BigDecimal(301.05, new MathContext(5)));
	}
    /*
     * RG_Compta_2
     * Pour qu'une écriture comptable soit valide, elle doit être équilibrée :
     * la somme des montants au crédit des lignes d'écriture doit être égale à la somme des montants au débit
     */
    @Test
    public void isEquilibree_doitEtreEquilibree()
    {
    	// arrange  
    	ecritueUnderTest.setLibelle("Equilibrée");
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(1, "200.50", null));
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(1, "100.50", "33"));
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "301"));
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(2, "40", "7"));
        
        // act
        boolean actualResult = ecritueUnderTest.isEquilibree();
        
        // assert
        assertThat(actualResult).isTrue();
    }
    
    @Test
    public void isEquilibree_neDoitPasEtreEquilibree()
    {
    	// arrange
    	ecritueUnderTest.setLibelle("Non équilibrée");
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(1, "10", null));
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(1, "20", "1"));
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(2, null, "30"));
    	ecritueUnderTest.getListLigneEcriture().add(this.createLigne(2, "1", "2"));
        
        // act
        boolean actualResult = ecritueUnderTest.isEquilibree();
        
        // assert
        assertThat(actualResult).isFalse();
    }
    
    /*
     * RG_Compta_3
     * Une écriture comptable doit contenir au moins deux lignes d'écriture : une au débit et une au crédit
     */
    
    /*
     * RG_Compta_5
     * La référence d'une écriture comptable est composée du code du journal dans lequel figure l'écriture suivi de l'année et
     * d'un numéro de séquence (propre à chaque journal) sur 5 chiffres incrémenté automatiquement à chaque écriture
     * Le formatage de la référence est : XX-AAAA/#####
     * Ex : Journal de banque (BQ), écriture au 31/12/2016--> BQ-2016/00001
     */
    
    /*
     * RG_Compta_6
     * La référence d'une écriture comptable doit être unique, il n'est pas possible de créer plusieurs écritures ayant la même référence
     */
    
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
