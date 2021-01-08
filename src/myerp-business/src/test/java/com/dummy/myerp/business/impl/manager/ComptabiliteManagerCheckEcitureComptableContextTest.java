package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dummy.myerp.business.impl.AbstractBusinessManager;
import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.CompteComptable;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.model.bean.comptabilite.JournalComptable;
import com.dummy.myerp.model.bean.comptabilite.LigneEcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ComptabiliteManagerCheckEcitureComptableContextTest
{
	@Mock private EcritureComptable ecritureComptableMock;
	@Mock private DaoProxy daoProxyMock;
	@Mock private ComptabiliteDao comptabiliteDaoMock;
	
	private ComptabiliteManagerImpl managerUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
    	managerUnderTest = Mockito.spy(new ComptabiliteManagerImpl());
    	AbstractBusinessManager.configure(null, daoProxyMock, null);
	}
	
	@AfterEach
	void CleanUp()
	{
		managerUnderTest = null;
	}
	
	@Test
	public void checkEcritureComptableContext_WithoutReference_ShouldNotRaiseException() throws Exception
	{
		// arrange
		Mockito.when(ecritureComptableMock.getReference()).thenReturn(StringUtils.EMPTY);
	    
	    // act
	    managerUnderTest.checkEcritureComptableContext(ecritureComptableMock);
	    
	    // assert
	    Mockito.verify(ecritureComptableMock).getReference(); // Fin de la méthode checkEcritureComptableContext atteinte  
	  }

	@Test
	public void checkEcritureComptableContext_NotFound_ShouldNotRaiseNotFoundException() throws Exception
	{
		// arrange
		Mockito.when(ecritureComptableMock.getReference()).thenReturn("BQ-2020/00001");
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getEcritureComptableByRef(Mockito.anyString())).thenThrow(new NotFoundException());
		
		// act
		managerUnderTest.checkEcritureComptableContext(ecritureComptableMock);

		// assert
		Mockito.verify(comptabiliteDaoMock).getEcritureComptableByRef(Mockito.anyString());
	}
	
	@Test
	public void checkEcritureComptableContext_WithNullId_ShouldRaiseException() throws Exception
	{
		// arrange
		Mockito.when(ecritureComptableMock.getReference()).thenReturn("BQ-2020/00001");
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureComptableMock);
		Mockito.when(ecritureComptableMock.getId()).thenReturn(null);
		 
		// assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableContext(ecritureComptableMock);})
        	.withMessage("Une autre écriture comptable existe déjà avec la même référence.");
	}

	@Test
	public void checkEcritureComptableContext_WithNotSameId_ShouldRaiseException() throws Exception
	{
		// arrange
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureComptableMock);
		Mockito.when(ecritureComptableMock.getId()).thenReturn(10);
		 
		EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(20);
        vEcritureComptable.setReference("Dummy");

		// assert
        assertThatExceptionOfType(FunctionalException.class).isThrownBy(() -> {
        	managerUnderTest.checkEcritureComptableContext(vEcritureComptable);})
        	.withMessage("Une autre écriture comptable existe déjà avec la même référence.");
	}
	
	@Test
	public void checkEcritureComptableContext_ShoulPass() throws Exception
	{
		// arrange
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureComptableMock);
		Mockito.when(ecritureComptableMock.getId()).thenReturn(10);
		 
		EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(10);
        vEcritureComptable.setReference("Dummy");
        
		// act
		managerUnderTest.checkEcritureComptableContext(vEcritureComptable);
        
		// assert
		Mockito.verify(ecritureComptableMock).getId();
	}
	
	@Test
	public void checkEcritureComptable_ShouldCall2Methods() throws Exception
	{
		// arrange
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureComptableMock);
		Mockito.when(ecritureComptableMock.getId()).thenReturn(10);
		
		EcritureComptable vEcritureComptable = Mockito.spy(new EcritureComptable());
		vEcritureComptable.setId(10);
		vEcritureComptable.setJournal(new JournalComptable("AC", "Achat"));
		vEcritureComptable.setDate(new Date());
		vEcritureComptable.setLibelle("Libelle");
		Calendar calendar = Calendar.getInstance();
		String annee = String.valueOf(calendar.get(Calendar.YEAR));
		vEcritureComptable.setReference("AC-" + annee + "/00001");
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(1), null, new BigDecimal(123), null));
		vEcritureComptable.getListLigneEcriture().add(new LigneEcritureComptable(new CompteComptable(2),null, null, new BigDecimal(123)));
		
		// act
		managerUnderTest.checkEcritureComptable(vEcritureComptable);
		
		// assert
		Mockito.verify(managerUnderTest).checkEcritureComptableUnit(Mockito.any(EcritureComptable.class));
		Mockito.verify(managerUnderTest).checkEcritureComptableContext(Mockito.any(EcritureComptable.class));
	}
}
