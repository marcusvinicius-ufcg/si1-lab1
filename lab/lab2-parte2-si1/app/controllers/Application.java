package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import models.ComparatorPrioridade;
import models.ComparatorSemana;
import models.Meta;
import models.DAO.GenericDAO;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.*;
import views.html.*;
import play.db.jpa.Transactional;

public class Application extends Controller {
	static GenericDAO dao = new GenericDAO();
	
	static{
		try{
			
			String[] metas = {"Java", "Banco de Dados", "Cloud Computing",
					"Desenvolvimento Web", "Aplicações Android", "Inglês",
					"C++", "Play Framework", "Ruby", "Python"};
			
			Random random = new Random();
			
			for (int i = 0; i < metas.length; i++) {
				Calendar data = new GregorianCalendar();
				
				
				data.set(Calendar.WEEK_OF_YEAR, data.get(Calendar.WEEK_OF_YEAR) + random.nextInt(7));
				Meta meta = new Meta();
				int prioridade = random.nextInt(4);
				meta.setPrioridade(prioridade +1);
				
				meta.setDataFinalizacao(data.getTime());
				meta.setStatus(false);
				meta.setDescricao("Estudar " + metas[i]);
				dao.persist(meta);
				dao.flush();
			}			
			
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
	
	@Transactional
	public static Result index() {
		
		List<Meta> metas = dao.findAllByClassName("Meta");
		Collections.sort(metas, new ComparatorSemana());
		
		List<Long> semanas = new ArrayList<>();
		
		long aux = 0;
		for (Meta meta : metas) {
			if(aux != meta.getSemana()){
				aux = meta.getSemana();
				semanas.add(aux);
			}
		}
		Collections.sort(metas, new ComparatorPrioridade());
		Collections.sort(semanas);
		
		return ok(index.render(metas, semanas));
	}
	@Transactional
	public static Result editar() {
		List<Meta> metas = dao.findAllByClassName("Meta");
		Collections.sort(metas);
			
		return ok(editar.render(metas));
	}

	public static Result cadastro() {
		Form<Meta> form = Form.form(Meta.class);
		return ok(cadastro.render(form));
	}
	@Transactional
	public static Result cadastrar() {

		Form<Meta> form = Form.form(Meta.class).bindFromRequest();

		if (form.hasErrors()) {
			return badRequest(cadastro.render(form));
		}
		Meta meta = form.get();
		dao.persist(meta);
		dao.flush();

		return redirect(routes.Application.index());
	}
	@Transactional
	public static Result abrirEdicao() {
		
		DynamicForm requestData = Form.form().bindFromRequest();
		long id = Long.parseLong(requestData.get("ID"));
		
		
		Form<Meta> metaForm = Form.form(Meta.class).fill(dao.findByEntityId(Meta.class, id));

		return ok(alterar.render(id, metaForm));
	}
	@Transactional
	public static Result alterar(Long id) {
		Form.form(Meta.class).fill(dao.findByEntityId(Meta.class, id));
		
		Form<Meta> alterarForm = Form.form(Meta.class).bindFromRequest();
		if (alterarForm.hasErrors()) {
			return badRequest(alterar.render(id, alterarForm));
		}
		
		dao.merge(alterarForm.get());
		dao.flush();
		return redirect(routes.Application.editar());
	}
	@Transactional
	public static Result abrirRemover() {

		DynamicForm requestData = Form.form().bindFromRequest();
		long id = Long.parseLong(requestData.get("ID"));

		Form<Meta> metaForm = Form.form(Meta.class).fill(dao.findByEntityId(Meta.class, id));

		return ok(remover.render(id, metaForm));
	}
	@Transactional
	public static Result remover(Long id) {

		dao.removeById(Meta.class, id);
		dao.flush();
		return redirect(routes.Application.editar());
	}

}
