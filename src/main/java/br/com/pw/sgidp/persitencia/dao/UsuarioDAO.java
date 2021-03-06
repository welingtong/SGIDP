package br.com.pw.sgidp.persitencia.dao;

import java.util.Collection;

import javax.persistence.Query;

import br.com.pw.sgidp.negocio.entidade.Usuario;
import br.com.pw.sgidp.persitencia.DAOAbstrato;

public class UsuarioDAO extends DAOAbstrato<Usuario> {

	public void inserir(Usuario entidade) {
		getSession().persist(entidade);
	}

	public void atualizar(Usuario entidade) {
		getSession().persist(getSession().merge(entidade));
	}

	public void excluir(Usuario entidade) {

		getSession().remove(getSession().merge(entidade));
	}

	public Usuario obterPorId(Long id) {
		return getSession().find(Usuario.class, id);
	}

	@SuppressWarnings("unchecked")
	public Collection<Usuario> consultarTodos() {
		Query query = getSession().createQuery("from Usuario");
		return query.getResultList();
	}

	public Usuario buscaUsuarioPorLoginESenha(Usuario usuario) {
		Query query = getSession()
				.createQuery(
						"from Usuario usuario where usuario.login=:login and usuario.senha=:senha");
		query.setParameter("login", usuario.getLogin());
		query.setParameter("senha", usuario.getSenha());
		try {
			return (Usuario) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<Usuario> buscaUsuarioPorFiltro(String tipoFiltro,
			String parametro) {

		Query query = getSession().createQuery(
				"from Usuario usuario where usuario." + tipoFiltro
						+ " like :parametro");
		query.setParameter("parametro", "%" + parametro + "%");
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Usuario> buscaUsuarioPorLogin(Usuario usuario) {
		Query query = getSession()
				.createQuery(
						"from Usuario usuario where usuario.login=:login and usuario.id<>:id");
		query.setParameter("login", usuario.getLogin());
		if (usuario.getId() == null) {
			query.setParameter("id", new Long(0));
		} else {
			query.setParameter("id", usuario.getId());
		}
		return query.getResultList();

	}

}
