package sgidp.web.template;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.PageParameters;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import sgidp.web.parlamentar.ListarParlamentarPage;
import sgidp.web.usuario.ListarUsuarioPage;
import br.com.pw.sgidp.negocio.entidade.Permissao;
import br.com.pw.sgidp.negocio.entidade.Usuario;

import com.pw.SessaoWeb;

public class BasePage extends WebPage {
	private static final String ABERTO = "aberto";
	private static final String FECHADO = "fechado";
	private static String moduloSelecionado = "";

	public BasePage(final PageParameters parameters, String modulo,
			String pagina) {
		if (!parameters.getString("link", "").equals("")) {
			moduloSelecionado = parameters.getString("link", "");
		}
		montaFormulario(modulo, pagina);
	}

	public BasePage(String modulo, String pagina) {
		montaFormulario(modulo, pagina);
	}

	private void montaFormulario(String modulo, String pagina) {
		add(new Label("migalha", "Onde estou: " + modulo + " -> " + pagina));
		Usuario usuario = ((SessaoWeb) Session.get()).getUsuarioLogado();
		String nomeUsuarioLogado = "";

		Map<String, String> mapaPermissoes = new HashMap<String, String>();
		if (usuario != null) {
			String[] arrayNome = usuario.getNome().split(" ");
			nomeUsuarioLogado = arrayNome[0];

			mapaPermissoes = getMapaPermissoes(usuario.getListaPermissao());
		}
		add(new Label("usuarioLogado", nomeUsuarioLogado));

		boolean isTemPermissao = false;
		// Usuario

		isTemPermissao = mapaPermissoes.containsKey("01") ? true : false;
		add(markupContainerManterParlamentar(ABERTO, isTemPermissao));
		add(markupContainerManterParlamentar(FECHADO, isTemPermissao));

		isTemPermissao = mapaPermissoes.containsKey("02") ? true : false;
		add(markupContainerManterUsuario(ABERTO, isTemPermissao));
		add(markupContainerManterUsuario(FECHADO, isTemPermissao));

	}

	private Map<String, String> getMapaPermissoes(
			Collection<Permissao> listaPermissao) {

		Map<String, String> mapaPermissao = new HashMap<String, String>();

		for (Permissao permissao : listaPermissao) {
			mapaPermissao.put(permissao.getCodigo(), permissao.getCodigo());
		}

		return mapaPermissao;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	private WebMarkupContainer markupContainerManterUsuario(
			String descricaoDiv, boolean isTemPermissao) {
		WebMarkupContainer webMarkupContainer = new WebMarkupContainer(
				"div_manter_usuario_" + descricaoDiv);
		webMarkupContainer.add(new BookmarkablePageLink("manterUsuarioLink",
				ListarUsuarioPage.class).setParameter("link", "02"));

		verificaMarkup("02", descricaoDiv, isTemPermissao, webMarkupContainer);

		return webMarkupContainer;
	}

	@SuppressWarnings( { "rawtypes", "unchecked" })
	private WebMarkupContainer markupContainerManterParlamentar(
			String descricaoDiv, boolean isTemPermissao) {
		WebMarkupContainer webMarkupContainer = new WebMarkupContainer(
				"div_manter_parlamentar_" + descricaoDiv);
		webMarkupContainer.add(new BookmarkablePageLink(
				"manterParlamentarLink", ListarParlamentarPage.class)
				.setParameter("link", "01"));

		verificaMarkup("01", descricaoDiv, isTemPermissao, webMarkupContainer);
		return webMarkupContainer;
	}

	private void verificaMarkup(String modulo, String descricaoDiv,
			boolean isTemPermissao, WebMarkupContainer webMarkupContainer) {
		if (isTemPermissao) {
			if (moduloSelecionado.equals(modulo)) {
				if (descricaoDiv.equals(FECHADO)) {
					webMarkupContainer.setVisible(false);
				}
			} else {
				if (descricaoDiv.equals(ABERTO)) {
					webMarkupContainer.setVisible(false);
				}
			}
		} else {
			webMarkupContainer.setVisible(false);
		}
	}
}
