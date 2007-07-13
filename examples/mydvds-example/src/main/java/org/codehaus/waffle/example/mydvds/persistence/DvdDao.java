package org.codehaus.waffle.example.mydvds.persistence;

import org.codehaus.waffle.example.mydvds.model.Dvd;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import java.util.List;

public class DvdDao {

	private Session session;

	DvdDao(Session session) {
		this.session = session;
	}

	public void add(Dvd dvd) {
		//salva o dvd na banca de dados pela sessão do hibernate
		session.save(dvd);
	}

	@SuppressWarnings("unchecked")
	public List<Dvd> searchSimilarTitle(String title) {
		//cria um criterio baseado na classe Dvd e adiciona
		//a restrição "titulo" e devolve uma lista de dvds
		return session.createCriteria(Dvd.class).add(
				Restrictions.ilike("title", "%" + title + "%")).list();
	}
}
