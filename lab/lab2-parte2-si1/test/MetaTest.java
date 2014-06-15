import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import models.Meta;
import models.Exception.MetaException;

import org.junit.Before;
import org.junit.Test;

public class MetaTest {
	private Meta meta;
	
	
	@Before
	public void setup() {
		try {
			meta = new Meta();
			meta.setDescricao("Breve descrição");
			meta.setPrioridade(5);
			try {
				meta.setDataFinalizacao(new Date());
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (MetaException metaInvalida) {
			// ok
		}
	}

	@Test
	public void devePermitirCriarMeta() {
		// Meta é definida pela descrição, prioridade, e a semana;
		try {
			meta = new Meta();
			meta.setDescricao("Breve descrição");
			meta.setPrioridade(5);
			meta.setDataFinalizacao(new Date());
			
		} catch (MetaException metaInvalida) {
			fail("Nao deveria retornar Exception");
		}
	}
	
	@Test
	public void adicionarMetaSomenteNasProximasSeisSemanas() {
		// Metas so podem ser adicionadas para as proximas 6 semanas;
		Calendar data = new GregorianCalendar(2014, 07, 22);
		
		try {
			meta.setDataFinalizacao(data.getTime());
		} catch (MetaException e) {
			assertEquals(e.getMessage(), "Data acima de 6 Semanas nao é Permitida");
		}
	}
	@Test
	public void definirMetaComoConcluida() {
		// Permitir Definir meta como concluida
		
		assertFalse(meta.isStatus());
		meta.setStatus(true);
		assertTrue(meta.isStatus());
	}
}
