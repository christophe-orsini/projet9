package com.dummy.myerp.business.impl.manager;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dummy.myerp.consumer.dao.contrat.ComptabiliteDao;
import com.dummy.myerp.consumer.dao.contrat.DaoProxy;
import com.dummy.myerp.model.bean.comptabilite.EcritureComptable;
import com.dummy.myerp.technical.exception.FunctionalException;
import com.dummy.myerp.technical.exception.NotFoundException;

@ExtendWith(MockitoExtension.class)
class ComptabiliteManagerCheckEcitureComptableContext
{
	@Mock private EcritureComptable ecritureComptableMock;
	@Mock private DaoProxy daoProxyMock;
	@Mock private ComptabiliteDao comptabiliteDaoMock;
	
	private ComptabiliteManagerImpl managerUnderTest;
	
	@BeforeEach
	void setUp() throws Exception
	{
    	managerUnderTest = new ComptabiliteManagerImpl();
    	managerUnderTest.configure(null, daoProxyMock, null);
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
	public void checkEcritureComptableContext_ShouldNotRaiseNotFoundException() throws Exception
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
	public void checkEcritureComptableContext_WithNullId_ShouldNotRaiseException() throws Exception
	{
		// arrange
		Mockito.when(ecritureComptableMock.getReference()).thenReturn("BQ-2020/00001");
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureComptableMock);
		Mockito.when(ecritureComptableMock.getId()).thenReturn(null);
		 
		// act
		managerUnderTest.checkEcritureComptableContext(ecritureComptableMock);
        
		// assert
		Mockito.verify(ecritureComptableMock).getId();
	}

	@Test
	public void checkEcritureComptableContext_WithSameId_ShouldRaiseException() throws Exception
	{
		// arrange
		Mockito.when(daoProxyMock.getComptabiliteDao()).thenReturn(comptabiliteDaoMock);
		Mockito.when(comptabiliteDaoMock.getEcritureComptableByRef(Mockito.anyString())).thenReturn(ecritureComptableMock);
		Mockito.when(ecritureComptableMock.getId()).thenReturn(10);
		 
		EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(10);
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
		Mockito.when(ecritureComptableMock.getId()).thenReturn(20);
		 
		EcritureComptable vEcritureComptable = new EcritureComptable();
        vEcritureComptable.setId(10);
        vEcritureComptable.setReference("Dummy");
        
		// act
		managerUnderTest.checkEcritureComptableContext(vEcritureComptable);
        
		// assert
		Mockito.verify(ecritureComptableMock).getId();
	}
}
