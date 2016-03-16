package palmaslab.mapas.controller;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import palmaslab.mapas.repository.Alerta;
import palmaslab.mapas.repository.Buscaendereco;
import palmaslab.mapas.repository.Cliente;
import palmaslab.mapas.repository.ClienteRepository;
import palmaslab.mapas.repository.Completed_survey;
import palmaslab.mapas.repository.Completed_surveyRepository;
import palmaslab.mapas.repository.Credito_Extrair_Cliente;
import palmaslab.mapas.repository.Credito_clientes_banco;
import palmaslab.mapas.repository.Credito_clientes_banco_Repository;
import palmaslab.mapas.repository.Images;
import palmaslab.mapas.repository.LinhaNegLigar;
import palmaslab.mapas.repository.LinhaNegLigarRepository;
import palmaslab.mapas.repository.LinhaNegPagamento;
import palmaslab.mapas.repository.LinhaNegPagamentoRepository;
import palmaslab.mapas.repository.LinhaNegVisita;
import palmaslab.mapas.repository.LinhaNegVisitaRepository;
import palmaslab.mapas.repository.LocalFileManager;
import palmaslab.mapas.repository.Mudar_position_gps;
import palmaslab.mapas.repository.NegociacaoLigar;
import palmaslab.mapas.repository.NegociacaoLigarRepository;
import palmaslab.mapas.repository.NegociacaoPagamento;
import palmaslab.mapas.repository.NegociacaoVisita;
import palmaslab.mapas.repository.NegociacaoVisitaRepository;
import palmaslab.mapas.repository.PostoSaudeRepository;
import palmaslab.mapas.repository.PostoSaudeUtility;
import palmaslab.mapas.repository.Postosaude;
import palmaslab.mapas.repository.Project;
import palmaslab.mapas.repository.ProjectRepository;
import palmaslab.mapas.repository.RelatorioPagamento;
import palmaslab.mapas.repository.apachepoi_test;
import palmaslab.mapas.repository.parametros;
import palmaslab.mapas.repository.parametrosRepository;
import palmaslab.mapas.repository.seguimento_cliente;
import palmaslab.mapas.repository.survey_question;
import palmaslab.mapas.repository.survey_questionRepository;
import palmaslab.mapas.repository.NegociacaoPagamentoRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;




//import org.springframework.web.multipart.MultipartFile;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;



@Controller
public class myController {

	@Autowired
	private PostoSaudeRepository PostosSaude;
	@Autowired
	private ClienteRepository Clientes;
	@Autowired
	private ProjectRepository Projects;
	@Autowired
	private parametrosRepository parametros;
	@Autowired
	private survey_questionRepository Surveys;
	@Autowired
	private Completed_surveyRepository CompletedSurveys;
	@Autowired
	private Credito_clientes_banco_Repository CreditoClientesBanco;
	@Autowired
	private NegociacaoPagamentoRepository mNegociacaoPagamentoRepository;
	@Autowired
	private LinhaNegPagamentoRepository mLinhaNegPagamentoRepository;
	@Autowired
	private NegociacaoLigarRepository mNegociacaoLigarRepository; 
	@Autowired
	private LinhaNegLigarRepository mLinhaNegLigarRepository;
	@Autowired
	private NegociacaoVisitaRepository mNegociacaoVisitaRepository; 
	@Autowired
	private LinhaNegVisitaRepository mLinhaNegVisitaRepository;
	
	final String LOCAL_PHOTO_VAR_PATH = "/{cliente}/{projeto}/{LocalId}/{PictureId}/picture";
	//updateprova!!!!!
	
	@RequestMapping(value = "prova_youtube", method = RequestMethod.GET)
	public ModelAndView prova_youtube( ) throws IOException 
	{
		
		ModelAndView mModelAndView= new ModelAndView("prova_youtube");
		
		return mModelAndView;
}
//GIVE IMAGES PATH FOR ONE CLIENT BY ID
	
	@RequestMapping(value = "get_images_path/{id}", method = RequestMethod.GET)
	public @ResponseBody String  give_image(@PathVariable("id") long id) throws IOException 
	{
	
		LocalFileManager mLocalFileManager = LocalFileManager.get();
		Postosaude posto = PostosSaude.findById(id);
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
	     
		try {
				json = mapper.writeValueAsString(mLocalFileManager.getImagesFromLocal(PostosSaude.findById(id)));
			} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("generated JSON for id="+id+";"+json);
		return json;
}
	
	
// LOAD CUSTOMER INFO
	
	@RequestMapping(value = "cliente_credito", method = RequestMethod.GET)
	public ModelAndView redirect_cliente_credito(@RequestParam("id") long id,Principal p) throws IOException 
	{
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
		//SECURITY CONTROL
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(id));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		String idcontrato= "";
		
		Credito_clientes_banco ccb = CreditoClientesBanco.findById(id);
		for(int i=0;i<ccb.getSituacao().size();i++)
			if( ccb.getSituacao().get(i).equals("PERDA"))
			{
				idcontrato = ccb.getContrato().get(i);
			}
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(id,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(id,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(id,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita());
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
}
	//
	
	
	@RequestMapping(value = "addneg_pag", method = RequestMethod.POST)
	public ModelAndView adicionarNovaNegociacaoPagamento(@ModelAttribute NegociacaoPagamento new_neg_pag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		new_neg_pag.setIdcliente(idcliente);
		new_neg_pag.setIdcontrato(idcontrato);
		new_neg_pag.setValorinicial(new_neg_pag.getValorinicial().replace(",", "."));
		new_neg_pag.setValor_por_parcela(new_neg_pag.getValor_por_parcela().replace(",", "."));
		if(new_neg_pag.getPeriodicidade().equals("cada 1 mes"))
		{
			new_neg_pag.setPeriodicidade("30");
		}
		NegociacaoPagamento np =mNegociacaoPagamentoRepository.save(new_neg_pag);
		
		//ALGORITMO CRIAÇÃO DE PARCELAS!
				if(new_neg_pag.getNumeroparcelas().equals("Automatico"))
				{
					if((Double.parseDouble(new_neg_pag.getValorinicial()) / Double.parseDouble(new_neg_pag.getValor_por_parcela())) >0)
					{
						new_neg_pag.setNumeroparcelas((int)((Double.parseDouble(new_neg_pag.getValorinicial()) / Double.parseDouble(new_neg_pag.getValor_por_parcela()))+1)+"");
					}
					else
					{
						new_neg_pag.setNumeroparcelas((int)((Double.parseDouble(new_neg_pag.getValorinicial()) / Double.parseDouble(new_neg_pag.getValor_por_parcela())))+"");
					}
					  
					System.out.println("NumeroParcelas Automatico -> "+new_neg_pag.getNumeroparcelas());
					boolean ultimaparcela = false;
					for( int i=0; i<Integer.parseInt(new_neg_pag.getNumeroparcelas());i++)
					{
						LinhaNegPagamento lnp = new LinhaNegPagamento();
						lnp.setChecked(false);
						lnp.setComentario("");
						lnp.setIdnegociacao(np.getId());
						lnp.setValorrecebido("-");
						
						if(((Double.parseDouble(new_neg_pag.getValorinicial())) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela())))>0 && !ultimaparcela)
						{
							lnp.setValorparcela(new_neg_pag.getValor_por_parcela());
						}
						else if(((Double.parseDouble(new_neg_pag.getValorinicial())) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela())))<=0 && !ultimaparcela)
						{
							lnp.setValorparcela(""+Double.parseDouble(new_neg_pag.getValorinicial())%(i* Double.parseDouble(new_neg_pag.getValor_por_parcela())));
							ultimaparcela = true;
						}
						else
						{
							lnp.setValorparcela("0.0");
						}
						
						if(new_neg_pag.getPeriodicidade().equals("Não"))
						{
							lnp.setDataalerta("");
						}
						else
						{
							String period =new_neg_pag.getPeriodicidade().replace("cada ", "").replace(" dias","").replace(" mes", "");
							int periodicidade = 0;
							periodicidade = Integer.parseInt(period);
							lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio(),periodicidade*i));
							System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
						}	
						mLinhaNegPagamentoRepository.save(lnp);
						new_neg_pag.addLinhaNegPagamento(lnp);
						mNegociacaoPagamentoRepository.save(new_neg_pag);
					}
				}
				else if(new_neg_pag.getNumeroparcelas().equals("Não negociado"))
				{
					LinhaNegPagamento lnp = new LinhaNegPagamento();
					lnp.setChecked(false);
					lnp.setComentario("");
					lnp.setDataalerta(new_neg_pag.getDatainicio());
					lnp.setIdnegociacao(np.getId());
					lnp.setValorparcela(new_neg_pag.getValor_por_parcela());
					lnp.setValorrecebido("-");
					mLinhaNegPagamentoRepository.save(lnp);
					new_neg_pag.addLinhaNegPagamento(lnp);
					mNegociacaoPagamentoRepository.save(new_neg_pag);
				}
				else
				{
					//new_neg_pag.setNumeroparcelas( (int)((Double.parseDouble(new_neg_pag.getValorinicial()) / Double.parseDouble(new_neg_pag.getValor_por_parcela())) + 0.5d)+"");
					String numeroparcelas_string = new_neg_pag.getNumeroparcelas().replace("cada ", "").replace(" dias", "");
					
					int numeroparcelas = Integer.parseInt(numeroparcelas_string);
					System.out.println("NumeroParcelas  -> "+numeroparcelas);
					boolean ultimaparcela = false;
					for( int i=0; i<numeroparcelas;i++)
					{
						LinhaNegPagamento lnp = new LinhaNegPagamento();
						lnp.setChecked(false);
						lnp.setComentario("");
						lnp.setIdnegociacao(np.getId());
						lnp.setValorrecebido("-");
						
						if(((Double.parseDouble(new_neg_pag.getValorinicial())) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela())))>0 && !ultimaparcela)
						{
							lnp.setValorparcela(new_neg_pag.getValor_por_parcela());
						}
						else if(((Double.parseDouble(new_neg_pag.getValorinicial())) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela())))<=0 && !ultimaparcela)
						{
							lnp.setValorparcela(""+Double.parseDouble(new_neg_pag.getValorinicial())%(i* Double.parseDouble(new_neg_pag.getValor_por_parcela())));
							ultimaparcela = true;
						}
						else
						{
							lnp.setValorparcela("0.0");
						}
						
						if(new_neg_pag.getPeriodicidade().equals("Não"))
						{
							lnp.setDataalerta("");
						}
						else
						{
							String period =new_neg_pag.getPeriodicidade();
							int periodicidade = 0;
							period = period.replace("cada ", "");
							period = period.replace(" dias","");
							System.out.println("period = "+period);
							periodicidade = Integer.parseInt(period);
							lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio(),periodicidade*i));
							System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
						}	
						mLinhaNegPagamentoRepository.save(lnp);
						new_neg_pag.addLinhaNegPagamento(lnp);
						mNegociacaoPagamentoRepository.save(new_neg_pag);
					}
				}
				atualizarAlertas();
				
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	public String  sumarRestarDiasFecha(String datainicial,int dias)
	 {
	  Calendar calendar = Calendar.getInstance();
	  DateFormat Data = new SimpleDateFormat("dd/MM/yyyy");
	  
	  try {
		calendar.setTime(Data.parse(datainicial));
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} // Configuramos la fecha que se recibe
	  calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
		
	  
	  Date fechaFinal =calendar.getTime();
	  System.out.println("data sumarRestasDiasFecha = "+Data.format(fechaFinal));
	  return Data.format(fechaFinal);
		
	 }
	@RequestMapping(value = "editneg_pag", method = RequestMethod.POST)
	public ModelAndView editarNovaNegociacaoPagamento(@ModelAttribute NegociacaoPagamento new_neg_pag,@RequestParam("idneg_pag") long idneg_pag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		System.out.println("todas las linhas de neg");
		new_neg_pag.setTabela_pagamento(mNegociacaoPagamentoRepository.findById(idneg_pag).getTabela_pagamento());
		new_neg_pag.setIdcliente(idcliente);
		new_neg_pag.setIdcontrato(idcontrato);
		new_neg_pag.setId(idneg_pag);
		new_neg_pag.setValorinicial(new_neg_pag.getValorinicial().replace(",", "."));
		mNegociacaoPagamentoRepository.save(new_neg_pag);
		System.out.println("numeroparcelas = "+new_neg_pag.getNumeroparcelas());
		
		//ALGORITMO CRIAÇÃO DE PARCELAS!
		if(new_neg_pag.getNumeroparcelas().equals("Automatico"))
		{
			new_neg_pag.setNumeroparcelas( (int)((Double.parseDouble(new_neg_pag.getValorinicial()) / Double.parseDouble(new_neg_pag.getValor_por_parcela())) + 0.5d)+"");  
			System.out.println("NumeroParcelas Automatico -> "+new_neg_pag.getNumeroparcelas());
			boolean ultimaparcela = false;
			for( int i=0; i<Integer.parseInt(new_neg_pag.getNumeroparcelas());i++)
			{
				LinhaNegPagamento lnp = new LinhaNegPagamento();
				lnp.setChecked(false);
				lnp.setComentario("");
				lnp.setIdnegociacao(idneg_pag);
				lnp.setValorrecebido("-");
				
				if(Double.parseDouble(new_neg_pag.getValorinicial()) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela()))>0 && !ultimaparcela)
				{
					lnp.setValorparcela(new_neg_pag.getValor_por_parcela());
				}
				else if(Double.parseDouble(new_neg_pag.getValorinicial()) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela()))<0 && !ultimaparcela)
				{
					lnp.setValorparcela(""+Double.parseDouble(new_neg_pag.getValorinicial())%(i* Double.parseDouble(new_neg_pag.getValor_por_parcela())));
					ultimaparcela = true;
				}
				else
				{
					lnp.setValorparcela("0.0");
				}
				
				if(new_neg_pag.getPeriodicidade().equals("Não"))
				{
					lnp.setDataalerta("");
				}
				else
				{
					String period =new_neg_pag.getPeriodicidade().replace("cada ", "");
					int periodicidade = 0;
					if(new_neg_pag.getPeriodicidade().equals("cada 1 mes"))
					{
						new_neg_pag.setPeriodicidade("30");
					}
					periodicidade = Integer.parseInt(period);
					lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio(),periodicidade*i));
					System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
				}	
				mLinhaNegPagamentoRepository.save(lnp);
				new_neg_pag.addLinhaNegPagamento(lnp);
				mNegociacaoPagamentoRepository.save(new_neg_pag);
			}
		}
		else if(new_neg_pag.getNumeroparcelas().equals("Não negociado"))
		{
			LinhaNegPagamento lnp = new LinhaNegPagamento();
			lnp.setChecked(false);
			lnp.setComentario("");
			lnp.setDataalerta(new_neg_pag.getDatainicio());
			lnp.setIdnegociacao(idneg_pag);
			lnp.setValorparcela(new_neg_pag.getValor_por_parcela());
			lnp.setValorrecebido("-");
			mLinhaNegPagamentoRepository.save(lnp);
			new_neg_pag.addLinhaNegPagamento(lnp);
			mNegociacaoPagamentoRepository.save(new_neg_pag);
		}
		else
		{
			//new_neg_pag.setNumeroparcelas( (int)((Double.parseDouble(new_neg_pag.getValorinicial()) / Double.parseDouble(new_neg_pag.getValor_por_parcela())) + 0.5d)+"");
			String numeroparcelas_string = new_neg_pag.getNumeroparcelas().replace("cada ", "").replace(" dias", "");
			
			int numeroparcelas = Integer.parseInt(numeroparcelas_string);
			System.out.println("NumeroParcelas  -> "+numeroparcelas);
			boolean ultimaparcela = false;
			for( int i=0; i<numeroparcelas;i++)
			{
				LinhaNegPagamento lnp = new LinhaNegPagamento();
				lnp.setChecked(false);
				lnp.setComentario("");
				lnp.setIdnegociacao(idneg_pag);
				lnp.setValorrecebido("-");
				
				if(Double.parseDouble(new_neg_pag.getValorinicial()) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela()))>0 && !ultimaparcela)
				{
					lnp.setValorparcela(new_neg_pag.getValor_por_parcela());
				}
				else if(Double.parseDouble(new_neg_pag.getValorinicial()) - ((i + 1)* Double.parseDouble(new_neg_pag.getValor_por_parcela()))<0 && !ultimaparcela)
				{
					lnp.setValorparcela(""+Double.parseDouble(new_neg_pag.getValorinicial())%(i* Double.parseDouble(new_neg_pag.getValor_por_parcela())));
					ultimaparcela = true;
				}
				else
				{
					lnp.setValorparcela("0.0");
				}
				
				if(new_neg_pag.getPeriodicidade().equals("Não"))
				{
					lnp.setDataalerta("");
				}
				else
				{
					String period =new_neg_pag.getPeriodicidade();
					int periodicidade = 0;
					period = period.replace("cada ", "");
					period = period.replace(" dias","");
					System.out.println("period = "+period);
					periodicidade = Integer.parseInt(period);
					lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio(),periodicidade*i));
					System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
				}	
				mLinhaNegPagamentoRepository.save(lnp);
				new_neg_pag.addLinhaNegPagamento(lnp);
				mNegociacaoPagamentoRepository.save(new_neg_pag);
			}
		}
		atualizarAlertas();
		
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	public void atualizarAlertas()
	{
		//PAGAMENTO
		List<LinhaNegPagamento> list_neg_pag = Lists.newArrayList(mLinhaNegPagamentoRepository.findByChecked(false));
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Date data_atual = new Date();
		sdf.format(data_atual);
		System.out.println("Data atual ="+sdf.format(data_atual));
    	try {
			for(LinhaNegPagamento neg_pag: list_neg_pag)
				{
					if(data_atual.compareTo(sdf.parse(neg_pag.getDataalerta())) >= 0)
					{
						neg_pag.setAlertaativa(true);
					}
				}
		
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mLinhaNegPagamentoRepository.save(list_neg_pag);
    	
    	//LIGAR
    	List<LinhaNegLigar> list_neg_ligar = Lists.newArrayList(mLinhaNegLigarRepository.findByChecked(false));
		
		
	
    	try {
			for(LinhaNegLigar neg_pag: list_neg_ligar)
				{
					if(data_atual.compareTo(sdf.parse(neg_pag.getDataalerta())) >= 0)
					{
						neg_pag.setAlertaativa(true);
					}
				}
		
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mLinhaNegLigarRepository.save(list_neg_ligar);
    	
    	//VISITA
    	List<LinhaNegVisita> list_neg_visita = Lists.newArrayList(mLinhaNegVisitaRepository.findByChecked(false));
		
		
    	try {
			for(LinhaNegVisita neg_pag: list_neg_visita)
				{
					if(data_atual.compareTo(sdf.parse(neg_pag.getDataalerta())) >= 0)
					{
						neg_pag.setAlertaativa(true);
					}
				}
		
    	} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	mLinhaNegVisitaRepository.save(list_neg_visita);
	}
	@RequestMapping(value = "salvarnegociacaoparcela", method = RequestMethod.GET)
	public ModelAndView salvarNegociacaoParcela(@RequestParam("dataalerta") String dataalerta,@RequestParam("valorparcela") String valorparcela,@RequestParam("valorrecebido") String valorrecebido,@RequestParam("comentario")String comentario,@RequestParam("idlinhanegpag") long idlinhanegpag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		System.out.println("dataalerta ="+dataalerta);
		System.out.println("valorparcela"+valorparcela);
		System.out.println("valorrecebido="+valorrecebido);
		System.out.println("comentario="+comentario);
		System.out.println("idlinhanegpag="+idlinhanegpag);
		System.out.println("idcliente="+idcliente);
		System.out.println("idcontrato="+idcontrato);
		
		LinhaNegPagamento mLinhaNegPagamento = mLinhaNegPagamentoRepository.findById(idlinhanegpag);
		mLinhaNegPagamento.setAlertaativa(false);
		mLinhaNegPagamento.setChecked(true);
		mLinhaNegPagamento.setComentario(comentario);
		mLinhaNegPagamento.setDataalerta(dataalerta);
		mLinhaNegPagamento.setValorparcela(valorparcela);
		mLinhaNegPagamento.setValorrecebido(valorrecebido);
		
		mLinhaNegPagamentoRepository.save(mLinhaNegPagamento);
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	@RequestMapping(value = "editarnegociacaoparcela", method = RequestMethod.GET)
	public ModelAndView editarnegociacaoparcela(@RequestParam("dataalerta") String dataalerta,@RequestParam("valorparcela") String valorparcela,@RequestParam("valorrecebido") String valorrecebido,@RequestParam("comentario")String comentario,@RequestParam("idlinhanegpag") long idlinhanegpag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		
		System.out.println("-> editarnegociacaoparcela");
		LinhaNegPagamento mLinhaNegPagamento = mLinhaNegPagamentoRepository.findById(idlinhanegpag);
		mLinhaNegPagamento.setAlertaativa(false);
		mLinhaNegPagamento.setChecked(false);
		mLinhaNegPagamento.setComentario(comentario);
		mLinhaNegPagamento.setDataalerta(dataalerta);
		mLinhaNegPagamento.setValorparcela(valorparcela);
		mLinhaNegPagamento.setValorrecebido(valorrecebido);
		
		mLinhaNegPagamentoRepository.save(mLinhaNegPagamento);
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	@RequestMapping(value = "deletarnegociacaoparcela", method = RequestMethod.GET)
	public ModelAndView deletarnegociacaoparcela(@RequestParam("idnegpag") long idnegpag,@RequestParam("idlinhanegpag") long idlinhanegpag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
	
		System.out.println("-> deletarnegociacaoparcela");
		NegociacaoPagamento np = mNegociacaoPagamentoRepository.findById(idnegpag);
		np.deleteLinhaNegPagamento(mLinhaNegPagamentoRepository.findById(idlinhanegpag));
		mNegociacaoPagamentoRepository.save(np);
		mLinhaNegPagamentoRepository.delete(mLinhaNegPagamentoRepository.findById(idlinhanegpag));
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	@RequestMapping(value = "adicionarnegociacaoparcela", method = RequestMethod.GET)
	public ModelAndView adicionarnegociacaoparcela(@RequestParam("id_neg_pag") long id_neg_pag,@RequestParam("dataalerta") String dataalerta,@RequestParam("valorparcela") String valorparcela,@RequestParam("valorrecebido") String valorrecebido,@RequestParam("comentario")String comentario,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		
		System.out.println("-> adicionarnegociacaoparcela");
		LinhaNegPagamento mLinhaNegPagamento = new LinhaNegPagamento();
		
		mLinhaNegPagamento.setAlertaativa(false);
		mLinhaNegPagamento.setChecked(false);
		mLinhaNegPagamento.setComentario(comentario);
		mLinhaNegPagamento.setDataalerta(dataalerta);
		mLinhaNegPagamento.setValorparcela(valorparcela);
		mLinhaNegPagamento.setValorrecebido(valorrecebido);
		mLinhaNegPagamento.setIdnegociacao(id_neg_pag);
		
		NegociacaoPagamento np = mNegociacaoPagamentoRepository.findById(id_neg_pag);
		np.addLinhaNegPagamento(mLinhaNegPagamento);
		mLinhaNegPagamentoRepository.save(mLinhaNegPagamento);
		mNegociacaoPagamentoRepository.save(np);
		
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	//LIGAR
	@RequestMapping(value = "addneg_ligar", method = RequestMethod.POST)
	public ModelAndView adicionarNovaNegociacaoLigar(@ModelAttribute NegociacaoLigar new_neg_pag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		System.out.println("new_neg_pag.getDatainicio_ligar() ->"+new_neg_pag.getDatainicio_ligar());
		System.out.println("new_neg_pag.getPeriodicidade_ligar() ->"+new_neg_pag.getPeriodicidade_ligar());
		new_neg_pag.setIdcliente(idcliente);
		new_neg_pag.setIdcontrato(idcontrato);
		NegociacaoLigar nl = mNegociacaoLigarRepository.save(new_neg_pag);
		
		String period =new_neg_pag.getPeriodicidade_ligar();
		int periodicidade;
		if(period.equals("Não"))
		{
			LinhaNegLigar lnp = new LinhaNegLigar();
			lnp.setChecked(false);
			lnp.setComentario("");
			lnp.setIdnegociacao(nl.getId());
			lnp.setDataalerta(new_neg_pag.getDatainicio_ligar());
			mLinhaNegLigarRepository.save(lnp);
			new_neg_pag.addLinhaNegLigar(lnp);
			mNegociacaoLigarRepository.save(new_neg_pag);
		}	
		else
		{
			period = period.replace("cada ", "");
			period = period.replace(" dias","");
			if(period.equals("cada 1 mes"))
			{
				period="30";
			}
			System.out.println("period = "+period);
			periodicidade = Integer.parseInt(period);
			int numero_repeticoes =1;
			if(new_neg_pag.getNumero_repeticoes_ligar().equals("Automatico"))
			{//OLHAR SE TEM NUMERO DE PARCELAS!!
				if(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato) !=  null)
				{
					numero_repeticoes = Integer.parseInt(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato).getNumeroparcelas());
				}
			}
			else
			{
				numero_repeticoes = Integer.parseInt(new_neg_pag.getNumero_repeticoes_ligar());
			}
			for( int i=0; i<numero_repeticoes;i++)
			{
				LinhaNegLigar lnp = new LinhaNegLigar();
				lnp.setChecked(false);
				lnp.setComentario("");
				lnp.setIdnegociacao(nl.getId());
				lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio_ligar(),periodicidade*i));
				System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
					
				mLinhaNegLigarRepository.save(lnp);
				new_neg_pag.addLinhaNegLigar(lnp);
				mNegociacaoLigarRepository.save(new_neg_pag);
			}
		}	
				
		
		atualizarAlertas();
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	@RequestMapping(value = "editneg_ligar", method = RequestMethod.POST)
	public ModelAndView editarNovaNegociacaoLigar(@ModelAttribute NegociacaoLigar new_neg_pag,@RequestParam("idneg_ligar") long idneg_pag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		System.out.println("todas las linhas de neg ligar");
		new_neg_pag.setTabela_ligar(mNegociacaoLigarRepository.findById(idneg_pag).getTabela_ligar());
		new_neg_pag.setIdcliente(idcliente);
		new_neg_pag.setIdcontrato(idcontrato);
		new_neg_pag.setId(idneg_pag);
		
		NegociacaoLigar nl = mNegociacaoLigarRepository.save(new_neg_pag);
		String period =new_neg_pag.getPeriodicidade_ligar();
		int periodicidade;
		if(period.equals("Não"))
		{
			LinhaNegLigar lnp = new LinhaNegLigar();
			lnp.setChecked(false);
			lnp.setComentario("");
			lnp.setIdnegociacao(nl.getId());
			lnp.setDataalerta(new_neg_pag.getDatainicio_ligar());
			mLinhaNegLigarRepository.save(lnp);
			new_neg_pag.addLinhaNegLigar(lnp);
			mNegociacaoLigarRepository.save(new_neg_pag);
		}	
		else
		{
			period = period.replace("cada ", "");
			period = period.replace(" dias","");
			if(period.equals("cada 1 mes"))
			{
				period="30";
			}
			System.out.println("period = "+period);
			periodicidade = Integer.parseInt(period);
			int numero_repeticoes =1;
			if(new_neg_pag.getNumero_repeticoes_ligar().equals("Automatico"))
			{//OLHAR SE TEM NUMERO DE PARCELAS!!
				if(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato) !=  null)
				{
					numero_repeticoes = Integer.parseInt(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato).getNumeroparcelas());
				}
			}
			else
			{
				numero_repeticoes = Integer.parseInt(new_neg_pag.getNumero_repeticoes_ligar());
			}
			for( int i=0; i<numero_repeticoes;i++)
			{
				LinhaNegLigar lnp = new LinhaNegLigar();
				lnp.setChecked(false);
				lnp.setComentario("");
				lnp.setIdnegociacao(nl.getId());
				lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio_ligar(),periodicidade*i));
				System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
					
				mLinhaNegLigarRepository.save(lnp);
				new_neg_pag.addLinhaNegLigar(lnp);
				mNegociacaoLigarRepository.save(new_neg_pag);
			}
		}	
		atualizarAlertas();
		
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
		
	}
	@RequestMapping(value = "salvarnegociacaoligar", method = RequestMethod.GET)
	public ModelAndView salvarNegociacaoligar(@RequestParam("dataalerta") String dataalerta,@RequestParam("comentario")String comentario,@RequestParam("idlinhanegligar") long idlinhanegligar,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		System.out.println("dataalerta ="+dataalerta);
		System.out.println("comentario="+comentario);
		System.out.println("idlinhanegpag="+idlinhanegligar);
		System.out.println("idcliente="+idcliente);
		System.out.println("idcontrato="+idcontrato);
		
		LinhaNegLigar mLinhaNegLigar = mLinhaNegLigarRepository.findById(idlinhanegligar);
		mLinhaNegLigar.setAlertaativa(false);
		mLinhaNegLigar.setChecked(true);
		mLinhaNegLigar.setComentario(comentario);
		mLinhaNegLigar.setDataalerta(dataalerta);
	
		
		mLinhaNegLigarRepository.save(mLinhaNegLigar);
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	@RequestMapping(value = "editarnegociacaoligar", method = RequestMethod.GET)
	public ModelAndView editarnegociacaoligar(@RequestParam("dataalerta") String dataalerta,@RequestParam("comentario")String comentario,@RequestParam("idlinhanegligar") long idlinhanegligar,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		
		System.out.println("-> editarnegociacaoligar");
		LinhaNegLigar mLinhaNegLigar = mLinhaNegLigarRepository.findById(idlinhanegligar);
		mLinhaNegLigar.setAlertaativa(false);
		mLinhaNegLigar.setChecked(false);
		mLinhaNegLigar.setComentario(comentario);
		mLinhaNegLigar.setDataalerta(dataalerta);
		
		
		mLinhaNegLigarRepository.save(mLinhaNegLigar);
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	@RequestMapping(value = "deletarnegociacaoligar", method = RequestMethod.GET)
	public ModelAndView deletarnegociacaoligar(@RequestParam("idnegligar") long idnegligar,@RequestParam("idlinhanegligar") long idlinhanegligar,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
	
		System.out.println("-> deletarnegociacaoligar");
		NegociacaoLigar np = mNegociacaoLigarRepository.findById(idnegligar);
		np.deleteLinhaNegLigar(mLinhaNegLigarRepository.findById(idlinhanegligar));
		mNegociacaoLigarRepository.save(np);
		mLinhaNegLigarRepository.delete(mLinhaNegLigarRepository.findById(idlinhanegligar));
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	@RequestMapping(value = "adicionarnegociacaoligar", method = RequestMethod.GET)
	public ModelAndView adicionarnegociacaoligar(@RequestParam("id_neg_ligar") long id_neg_ligar,@RequestParam("dataalerta") String dataalerta,@RequestParam("comentario")String comentario,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		
		System.out.println("-> adicionarnegociacaoligar");
		LinhaNegLigar mLinhaNegLigar= new LinhaNegLigar();
		
		mLinhaNegLigar.setAlertaativa(false);
		mLinhaNegLigar.setChecked(false);
		mLinhaNegLigar.setComentario(comentario);
		mLinhaNegLigar.setDataalerta(dataalerta);
		mLinhaNegLigar.setIdnegociacao(id_neg_ligar);
		
		NegociacaoLigar np = mNegociacaoLigarRepository.findById(id_neg_ligar);
		np.addLinhaNegLigar(mLinhaNegLigar);
		mLinhaNegLigarRepository.save(mLinhaNegLigar);
		mNegociacaoLigarRepository.save(np);
		
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	//VISITA
	@RequestMapping(value = "addneg_visita", method = RequestMethod.POST)
	public ModelAndView adicionarNovaNegociacaoVisita(@ModelAttribute NegociacaoVisita new_neg_pag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		new_neg_pag.setIdcliente(idcliente);
		new_neg_pag.setIdcontrato(idcontrato);
		NegociacaoVisita nv = mNegociacaoVisitaRepository.save(new_neg_pag);
		
		String period =new_neg_pag.getPeriodicidade_visita();
		int periodicidade;
		if(period.equals("Não"))
		{
			LinhaNegVisita lnp = new LinhaNegVisita();
			lnp.setChecked(false);
			lnp.setComentario("");
			lnp.setIdnegociacao(nv.getId());
			lnp.setDataalerta(new_neg_pag.getDatainicio_visita());
			mLinhaNegVisitaRepository.save(lnp);
			new_neg_pag.addLinhaNegVisita(lnp);
			mNegociacaoVisitaRepository.save(new_neg_pag);
		}	
		else
		{
			period = period.replace("cada ", "");
			period = period.replace(" dias","");
			if(period.equals("cada 1 mes"))
			{
				period="30";
			}
			System.out.println("period = "+period);
			periodicidade = Integer.parseInt(period);
			int numero_repeticoes =1;
			if(new_neg_pag.getNumero_repeticoes_visita().equals("Automatico"))
			{//OLHAR SE TEM NUMERO DE PARCELAS!!
				if(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato) !=  null)
				{
					numero_repeticoes = Integer.parseInt(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato).getNumeroparcelas());
				}
			}
			else
			{
				numero_repeticoes = Integer.parseInt(new_neg_pag.getNumero_repeticoes_visita());
			}
			for( int i=0; i<numero_repeticoes;i++)
			{
				LinhaNegVisita lnp = new LinhaNegVisita();
				lnp.setChecked(false);
				lnp.setComentario("");
				lnp.setIdnegociacao(nv.getId());
				lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio_visita(),periodicidade*i));
				System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
					
				mLinhaNegVisitaRepository.save(lnp);
				new_neg_pag.addLinhaNegVisita(lnp);
				mNegociacaoVisitaRepository.save(new_neg_pag);
			}
		}	
		
		atualizarAlertas();
		
		
		
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	@RequestMapping(value = "editneg_visita", method = RequestMethod.POST)
	public ModelAndView editarNovaNegociacaoVisita(@ModelAttribute NegociacaoVisita new_neg_pag,@RequestParam("idneg_visita") long idneg_pag,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		System.out.println("todas las linhas de neg visita");
		new_neg_pag.setTabela_visita(mNegociacaoVisitaRepository.findById(idneg_pag).getTabela_visita());
		new_neg_pag.setIdcliente(idcliente);
		new_neg_pag.setIdcontrato(idcontrato);
		new_neg_pag.setId(idneg_pag);
		System.out.println("mNegociacaoVisitaRepository.findById(idneg_pag).getTabela_visita().size() = "+(mNegociacaoVisitaRepository.findById(idneg_pag).getTabela_visita()).size());
NegociacaoVisita nv = mNegociacaoVisitaRepository.save(new_neg_pag);
		
		String period =new_neg_pag.getPeriodicidade_visita();
		int periodicidade;
		if(period.equals("Não"))
		{
			LinhaNegVisita lnp = new LinhaNegVisita();
			lnp.setChecked(false);
			lnp.setComentario("");
			lnp.setIdnegociacao(nv.getId());
			lnp.setDataalerta(new_neg_pag.getDatainicio_visita());
			mLinhaNegVisitaRepository.save(lnp);
			new_neg_pag.addLinhaNegVisita(lnp);
			mNegociacaoVisitaRepository.save(new_neg_pag);
		}	
		else
		{
			period = period.replace("cada ", "");
			period = period.replace(" dias","");
			if(period.equals("cada 1 mes"))
			{
				period="30";
			}
			System.out.println("period = "+period);
			periodicidade = Integer.parseInt(period);
			int numero_repeticoes =1;
			if(new_neg_pag.getNumero_repeticoes_visita().equals("Automatico"))
			{//OLHAR SE TEM NUMERO DE PARCELAS!!
				if(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato) !=  null)
				{
					numero_repeticoes = Integer.parseInt(mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente, idcontrato).getNumeroparcelas());
				}
			}
			else
			{
				numero_repeticoes = Integer.parseInt(new_neg_pag.getNumero_repeticoes_visita());
			}
			for( int i=0; i<numero_repeticoes;i++)
			{
				LinhaNegVisita lnp = new LinhaNegVisita();
				lnp.setChecked(false);
				lnp.setComentario("");
				lnp.setIdnegociacao(nv.getId());
				lnp.setDataalerta(sumarRestarDiasFecha(new_neg_pag.getDatainicio_visita(),periodicidade*i));
				System.out.println("Data alerta gerada! = "+lnp.getDataalerta());
					
				mLinhaNegVisitaRepository.save(lnp);
				new_neg_pag.addLinhaNegVisita(lnp);
				mNegociacaoVisitaRepository.save(new_neg_pag);
			}
		}	
		atualizarAlertas();
		
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	@RequestMapping(value = "salvarnegociacaovisita", method = RequestMethod.GET)
	public ModelAndView salvarNegociacaovisita(@RequestParam("dataalerta") String dataalerta,@RequestParam("comentario")String comentario,@RequestParam("idlinhanegvisita") long idlinhanegvisita,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		System.out.println("dataalerta ="+dataalerta);
		System.out.println("comentario="+comentario);
		System.out.println("idlinhanegpag="+idlinhanegvisita);
		System.out.println("idcliente="+idcliente);
		System.out.println("idcontrato="+idcontrato);
		
		LinhaNegVisita mLinhaNegVisita = mLinhaNegVisitaRepository.findById(idlinhanegvisita);
		mLinhaNegVisita.setAlertaativa(false);
		mLinhaNegVisita.setChecked(true);
		mLinhaNegVisita.setComentario(comentario);
		mLinhaNegVisita.setDataalerta(dataalerta);
	
		
		mLinhaNegVisitaRepository.save(mLinhaNegVisita);
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	@RequestMapping(value = "editarnegociacaovisita", method = RequestMethod.GET)
	public ModelAndView editarnegociacaovisita(@RequestParam("dataalerta") String dataalerta,@RequestParam("comentario")String comentario,@RequestParam("idlinhanegvisita") long idlinhanegvisita,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		
		System.out.println("-> editarnegociacaovisita");
		LinhaNegVisita mLinhaNegVisita = mLinhaNegVisitaRepository.findById(idlinhanegvisita);
		mLinhaNegVisita.setAlertaativa(false);
		mLinhaNegVisita.setChecked(false);
		mLinhaNegVisita.setComentario(comentario);
		mLinhaNegVisita.setDataalerta(dataalerta);
		
		
		mLinhaNegVisitaRepository.save(mLinhaNegVisita);
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	
	@RequestMapping(value = "deletarnegociacaovisita", method = RequestMethod.GET)
	public ModelAndView deletarnegociacaovisita(@RequestParam("idnegvisita") long idnegvisita,@RequestParam("idlinhanegvisita") long idlinhanegvisita,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
	
		System.out.println("-> deletarnegociacaovisita");
		NegociacaoVisita np = mNegociacaoVisitaRepository.findById(idnegvisita);
		np.deleteLinhaNegVisita(mLinhaNegVisitaRepository.findById(idlinhanegvisita));
		mNegociacaoVisitaRepository.save(np);
		mLinhaNegVisitaRepository.delete(mLinhaNegVisitaRepository.findById(idlinhanegvisita));
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
	@RequestMapping(value = "adicionarnegociacaovisita", method = RequestMethod.GET)
	public ModelAndView adicionarnegociacaovisita(@RequestParam("id_neg_visita") long id_neg_visita,@RequestParam("dataalerta") String dataalerta,@RequestParam("comentario")String comentario,@RequestParam("idcliente") long idcliente,@RequestParam("idcontrato") String idcontrato,Principal p) throws IOException 
	{
		
		
		System.out.println("-> adicionarnegociacaovisita");
		LinhaNegVisita mLinhaNegVisita= new LinhaNegVisita();
		
		mLinhaNegVisita.setAlertaativa(false);
		mLinhaNegVisita.setChecked(false);
		mLinhaNegVisita.setComentario(comentario);
		mLinhaNegVisita.setDataalerta(dataalerta);
		mLinhaNegVisita.setIdnegociacao(id_neg_visita);
		
		NegociacaoVisita np = mNegociacaoVisitaRepository.findById(id_neg_visita);
		np.addLinhaNegVisita(mLinhaNegVisita);
		mLinhaNegVisitaRepository.save(mLinhaNegVisita);
		mNegociacaoVisitaRepository.save(np);
		
		
		ModelAndView mModelAndView= new ModelAndView("cliente_credito");
			
		mModelAndView.addObject("cliente",CreditoClientesBanco.findById(idcliente));
		mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
		
		
		if(!mNegociacaoPagamentoRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{
			mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
		if(!mNegociacaoLigarRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
		if(!mNegociacaoVisitaRepository.findByIdcliente(idcliente).isEmpty() && !idcontrato.equals(""))
		{	
			mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(idcliente,idcontrato));
		}	
		mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
		mModelAndView.addObject("Buscaendereco", new Buscaendereco());
		return mModelAndView;
	}
/*	@RequestMapping(value = "add_comentario_seguimiento_credito/{id}", method = RequestMethod.POST)
	public ModelAndView add_comentario_seguimiento_credito(@PathVariable("id")long id,@ModelAttribute seguimento_cliente s_c, Model model) 
	{
		
		
			Credito_clientes_banco ccb = CreditoClientesBanco.findById(id);
			ccb.addComentarios(s_c.getNovo_seguimento());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            ccb.addData_comentarios(dateFormat.format(date)); //2014/08/06 15:59:48
			CreditoClientesBanco.save(ccb);
			ModelAndView mModelAndView = new ModelAndView("cliente_credito");
			//SECURITY CONTROL
			mModelAndView.addObject("cliente",CreditoClientesBanco.findById(id));
			mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
			
		
			return mModelAndView;
	}*/
	@RequestMapping(value = "procuraporid_cliente_credito", method = RequestMethod.GET)
	public ModelAndView procuraporid_cliente_creditoGET(@RequestParam("id") long id) 
	{
		// USO A CLASSE SEGUIMENTO CLIENTE PARA PEGAR O ID!
			Credito_clientes_banco ccb_achado = null;
			System.out.println("Codigo do Cliente sem modificar = "+id);
			
		
			
			
			ModelAndView mModelAndView = new ModelAndView("cliente_credito");
			//SECURITY CONTROL
			
			mModelAndView.addObject("cliente",CreditoClientesBanco.findById(id));
			mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
			String idcontrato= "";
			
			Credito_clientes_banco ccb = CreditoClientesBanco.findById(id);
			
			for(int i=0;i<ccb.getSituacao().size();i++){
				if( ccb.getSituacao().get(i).equals("PERDA"))
				{
					idcontrato = ccb.getContrato().get(i);
				}
				else if(ccb.getSituacao().get(i).equals("ATIVO")){
					idcontrato = ccb.getContrato().get(i);
				}
			}
			
			if(!mNegociacaoPagamentoRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
			{
				
				System.out.println("mNegociacaoPagamentoRepository");
				mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(id,idcontrato));
			}	
			mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
			if(!mNegociacaoLigarRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
			{	
				System.out.println("mNegociacaoLigarRepository");
				List<NegociacaoLigar> lnl = Lists.newArrayList(mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(id,idcontrato));
				for(NegociacaoLigar n: lnl){
					System.out.println("Negociacao -> id "+n.getId());
					System.out.println("Negociacao -> id Cliente"+n.getIdcliente());
					System.out.println("Negociacao -> id Contrato"+n.getIdcontrato());
				}
				mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(id,idcontrato));
			}	
			mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
			
			if(!mNegociacaoVisitaRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
			{	
				System.out.println("mNegociacaoVisitaRepository");
				mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(id,idcontrato));
			}	
			mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
			mModelAndView.addObject("Buscaendereco", new Buscaendereco());
			return mModelAndView;
	}
	/*@RequestMapping(value = "delete_erro", method = RequestMethod.GET)
	public void delete_erro() 
	{
		long id = 3898;
		String idcontrato = "";
		Credito_clientes_banco ccb = CreditoClientesBanco.findById(id);
		for(int i=0;i<ccb.getSituacao().size();i++){
			if( ccb.getSituacao().get(i).equals("PERDA"))
			{
				idcontrato = ccb.getContrato().get(i);
			}
			else if(ccb.getSituacao().get(i).equals("ATIVO")){
				idcontrato = ccb.getContrato().get(i);
			}
		}
		List<NegociacaoLigar> lnl = Lists.newArrayList(mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(id,idcontrato));
		for(NegociacaoLigar nl : lnl){
		//	List<LinhaNegLigar> l = Lists.newArrayList(mLinhaNegLigarRepository.fin)
		mNegociacaoLigarRepository.delete(nl);
		}
		
		System.out.println(" Se supone que se ha borrado sin problemas");
		System.out.println("mLinhaNegLigarRepository.findAll()).size() = "+Lists.newArrayList(mLinhaNegLigarRepository.findAll()).size());
		System.out.println("mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(id,idcontrato).size = "+Lists.newArrayList(mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(id,idcontrato)));
	}*/
	@RequestMapping(value = "procuraporid_cliente_credito", method = RequestMethod.POST)
	public ModelAndView procuraporid_cliente_credito(@ModelAttribute seguimento_cliente s_c) 
	{
		// USO A CLASSE SEGUIMENTO CLIENTE PARA PEGAR O ID!
			Credito_clientes_banco ccb_achado = null;
			System.out.println("Codigo do Cliente sem modificar = "+s_c.getNovo_seguimento());
			while(s_c.getNovo_seguimento().length()<10)
			{
				s_c.setNovo_seguimento("0"+s_c.getNovo_seguimento());
			}
			System.out.println("Codigo do Cliente modificado = "+s_c.getNovo_seguimento());
			
			
			
			ModelAndView mModelAndView = new ModelAndView("cliente_credito");
			//SECURITY CONTROL
			long id = CreditoClientesBanco.findByCodigocliente(Long.parseLong(s_c.getNovo_seguimento())).getId();
			mModelAndView.addObject("cliente",CreditoClientesBanco.findByCodigocliente(Long.parseLong(s_c.getNovo_seguimento())));
			mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
			String idcontrato= "";
			
			Credito_clientes_banco ccb = CreditoClientesBanco.findByCodigocliente(Long.parseLong(s_c.getNovo_seguimento()));
			for(int i=0;i<ccb.getSituacao().size();i++){
				if( ccb.getSituacao().get(i).equals("PERDA"))
				{
					idcontrato = ccb.getContrato().get(i);
				}
				else if(ccb.getSituacao().get(i).equals("ATIVO")){
					idcontrato = ccb.getContrato().get(i);
				}
			}
			
			if(!mNegociacaoPagamentoRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
			{
				mModelAndView.addObject("neg_pag",mNegociacaoPagamentoRepository.findByIdclienteAndIdcontrato(id,idcontrato));
			}	
			mModelAndView.addObject("new_neg_pag",new NegociacaoPagamento()); 
			if(!mNegociacaoLigarRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
			{	
				mModelAndView.addObject("neg_ligar",mNegociacaoLigarRepository.findByIdclienteAndIdcontrato(id,idcontrato));
			}	
			mModelAndView.addObject("new_neg_ligar",new NegociacaoLigar()); 
			System.out.println("mNegociacaoVisitaRepository.findByIdcliente(id).isEmpty() = "+mNegociacaoVisitaRepository.findByIdcliente(id).isEmpty());
			System.out.println("idcontrato com Perda = "+idcontrato);
			if(!mNegociacaoVisitaRepository.findByIdcliente(id).isEmpty() && !idcontrato.equals(""))
			{	
				System.out.println("mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(id,idcontrato)"+mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(id,idcontrato));
				mModelAndView.addObject("neg_visita",mNegociacaoVisitaRepository.findByIdclienteAndIdcontrato(id,idcontrato));
			}	
			mModelAndView.addObject("new_neg_visita",new NegociacaoVisita()); 
			mModelAndView.addObject("Buscaendereco", new Buscaendereco());
			return mModelAndView;
	}
	
	@RequestMapping(value = "procuraporendereco", method = RequestMethod.POST)
	public ModelAndView procuraporendereco(@ModelAttribute Buscaendereco s_c) 
	{
			System.out.println("procuraporendereco -> getEndereco = "+s_c.getEndereco());
			ModelAndView mModelAndView = new ModelAndView("buscaendereco");
			System.out.println(" CreditoClientesBanco.Containing(s_c.getEndereco()).size() = "+CreditoClientesBanco.findByEnderecoContaining(s_c.getEndereco().toUpperCase()).size());
			mModelAndView.addObject("clientes",CreditoClientesBanco.findByEnderecoContaining(s_c.getEndereco().toUpperCase()));
			mModelAndView.addObject("Buscaendereco", new Buscaendereco());
			mModelAndView.addObject("logradouro", s_c.getEndereco());
			mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());			
			return mModelAndView;
	}
	
	
	
// LOAD REDIRECT MEDIA PAGE!		


	@RequestMapping(value = "localpoint", method = RequestMethod.GET)
	public ModelAndView redirect_LOCALPOINT(@RequestParam("id") long id,@RequestParam("pid") long pid,Principal p) throws IOException 
	{
		apachepoi_test mapachepoi_test = new apachepoi_test();
		mapachepoi_test.readFile();
		ModelAndView mModelAndView= new ModelAndView("localpoint");
		//SECURITY CONTROL
		List<String> clients = Projects.findById(pid).getClients_with_project();
		boolean verified =false;
		for(int i=0;i<clients.size();i++)
		{
			if(clients.get(i).equals(p.getName()))
			{
				verified = true;
			}
		}
		
		if(verified)
		{
			System.out.println("Is verified!!");
			LocalFileManager mLocalFileManager = LocalFileManager.get();
			Images image = new Images();
			Postosaude posto = PostosSaude.findById(id);
			mModelAndView.addObject("local",posto);
			mModelAndView.addObject("Images",image);
			//mModelAndView.addObject("id_local",id);
			mModelAndView.addObject("images_path",mLocalFileManager.getImagesFromLocal(PostosSaude.findById(id)));
			mModelAndView.addObject("survey",Projects.findById(pid).getAllSurveyQuestions());
			mModelAndView.addObject("project",Projects.findById(pid));
		/*	mModelAndView.addObject("youtube_links",posto.getYoutubeLinkVideos());
			mModelAndView.addObject("nome_posto_saude",posto.getNome_posto_saude());
			mModelAndView.addObject("latitude",posto.getLatitude());
			mModelAndView.addObject("longitude",posto.getLongitude());
			mModelAndView.addObject("endereco",posto.getEndereco());
			mModelAndView.addObject("barrio_ou_nome_municipio",posto.getBarrio_ou_nome_municipio());
			mModelAndView.addObject("cidade",posto.getCidade());
			mModelAndView.addObject("estado",posto.getEstado());
			mModelAndView.addObject("pais",posto.getPais());
			mModelAndView.addObject("descricao",posto.getDescricao());
			mModelAndView.addObject("lista_respostas",posto.getLista_respostas());*/
			
		//	mModelAndView.addObject("nome_posto_saude",Projects.findById(pid).get);
			
			
		}
		else
		{
			System.out.println("NNNNNOT verified!");
		}
		return mModelAndView;
}

	//CUSTOMIZED SITE FOR EACH POINT
	@RequestMapping(value = "redirect_media/{id}", method = RequestMethod.GET)
	public ModelAndView redirect_media_page(@PathVariable("id") long id) throws IOException 
	{
		
		ModelAndView mModelAndView= new ModelAndView("media_admin");
		LocalFileManager mLocalFileManager = LocalFileManager.get();
		Images image = new Images();
		Postosaude posto = PostosSaude.findById(id);
		mModelAndView.addObject("Images",image);
		mModelAndView.addObject("id_local",id);
		mModelAndView.addObject("images_path",mLocalFileManager.getImagesFromLocal(PostosSaude.findById(id)));
		mModelAndView.addObject("youtube_links",posto.getYoutubeLinkVideos());
		return mModelAndView;
}
	//UPLOAD A EXCEL FILE FROM CREDITO FORM
	
	//UPLOAD A PICTURE FROM ADMIN FORM	
	
	@RequestMapping(value = "upload_mapa_credito", method = RequestMethod.POST)
	public ModelAndView Update_mapa_credito(  @ModelAttribute("Images") Images images,BindingResult bindingResult,
                    Model model) throws IOException {

				LocalFileManager mLocalFileManager = LocalFileManager.get();
				
				
				mLocalFileManager.saveLocalData( images.getImage().getInputStream());
		//		CreditoClientesBanco.deleteAll();
		
				Credito_Extrair_Cliente cec = new Credito_Extrair_Cliente();
				CreditoClientesBanco.save(cec.lerArquivo(Lists.newArrayList(CreditoClientesBanco.findAll())));
				
				ModelAndView mModelAndView= new ModelAndView("mapa_credito");
				mModelAndView.addObject("clientes_banco",Lists.newArrayList(CreditoClientesBanco.findAll()));
				mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
				mModelAndView.addObject("Buscaendereco", new Buscaendereco());
				mModelAndView.addObject("alertas",getAlertas());
				return mModelAndView;

}
	
	
	//UPLOAD A PICTURE FROM ADMIN FORM	
	
		@RequestMapping(value = "upload", method = RequestMethod.POST)
		public ModelAndView addVocabularyValadate(@RequestParam long id, @ModelAttribute("Images") Images images,BindingResult bindingResult,
                        Model model) throws IOException {

					
					System.out.println("inputstream Nombre!"+images.getImage().getOriginalFilename());
					LocalFileManager mLocalFileManager = LocalFileManager.get();
					Postosaude g = PostosSaude.findById(id);
					
					mLocalFileManager.saveLocalData(g, g.getPicturesCount(), images.getImage().getInputStream());
					g.setPicturesCount(g.getPicturesCount()+1);
					PostosSaude.save(g);
					ModelAndView mModelAndView= new ModelAndView("media_admin");
					Images image = new Images();
					mModelAndView.addObject("Images",image);
					mModelAndView.addObject("id_local",id);
					mModelAndView.addObject("images_path",mLocalFileManager.getImagesFromLocal(PostosSaude.findById(id)));
					mModelAndView.addObject("youtube_links",PostosSaude.findById(id).getYoutubeLinkVideos());
					return mModelAndView;
					
			
			
	
}
		@RequestMapping(value = "picture", method = RequestMethod.GET)
		public ModelAndView PictureHTML(Principal p,HttpServletResponse response) throws IOException {
			
			ModelAndView mv = new ModelAndView("picture");
			  response.setContentType("text/html");
		     response.setCharacterEncoding("ISO-8859-1");
		     String json = "";
		     ObjectMapper mapper = new ObjectMapper();
		     Images image = new Images();
		     LocalFileManager mLocalFileManager = LocalFileManager.get();
		  //   Postosaude g = PostosSaude.findById(33);
		    // mLocalFileManager.copyGiftData(g, 0, response.getOutputStream());
			 mv.addObject("image",image);
		
			return mv;
		}		

		@RequestMapping(value = "image/"/*{idLocal}/{idImage}"*/, method = RequestMethod.GET)
		public void loadImage(@RequestParam("id") String idEncoded,Principal p,HttpServletResponse response) throws IOException {
			
			//Descoding idLocal and idImage from idEncoded: Example: idLocal-idImage, 33-1
			long idLocal = 0;
			int idImage = 0;
			System.out.println("idEncoded="+idEncoded);
			String aux = "";
			boolean idLocalGotten = false;
			for(int i = 0; i < idEncoded.length() ; i++)
			{
				if(!idEncoded.substring(i,i+1).equals("-"))
				{
					aux += idEncoded.substring(i,i+1) ;
				}
				else
				{
					if(!idLocalGotten)
					{
						idLocal = Integer.parseInt(aux);
						aux="";
						idLocalGotten = true;
					}
				}
			}
	
			idImage = Integer.parseInt(aux);
			
			 response.setContentType("text/html");
		     response.setCharacterEncoding("ISO-8859-1");
		    
		     LocalFileManager mLocalFileManager = LocalFileManager.get();
		     Postosaude g = PostosSaude.findById(idLocal);
		     mLocalFileManager.copyGiftData(g, idImage, response.getOutputStream());
			//	mv.addObject("image",);
		
			//return mv;
		}		
		//SMALL IMAGE!
		@RequestMapping(value = "SMALLimage/"/*{idLocal}/{idImage}"*/, method = RequestMethod.GET)
		public void loadSmallImage(@RequestParam("id") String idEncoded,Principal p,HttpServletResponse response) throws IOException {
			
			//Descoding idLocal and idImage from idEncoded: Example: idLocal-idImage, 33-1
			long idLocal = 0;
			int idImage = 0;
			System.out.println("idEncoded="+idEncoded);
			String aux = "";
			boolean idLocalGotten = false;
			for(int i = 0; i < idEncoded.length() ; i++)
			{
				if(!idEncoded.substring(i,i+1).equals("-"))
				{
					aux += idEncoded.substring(i,i+1) ;
				}
				else
				{
					if(!idLocalGotten)
					{
						idLocal = Integer.parseInt(aux);
						aux="";
						idLocalGotten = true;
					}
				}
			}
	
			idImage = Integer.parseInt(aux);
			
			 response.setContentType("text/html");
		     response.setCharacterEncoding("ISO-8859-1");
		    
		     LocalFileManager mLocalFileManager = LocalFileManager.get();
		     Postosaude g = PostosSaude.findById(idLocal);
		     mLocalFileManager.copySmallGiftData(g, idImage, response.getOutputStream());
			//	mv.addObject("image",);
		
			//return mv;
		}	
		
		@RequestMapping(value = "image_background/"/*{idLocal}/{idImage}"*/, method = RequestMethod.GET)
		public void loadImageBackground(@RequestParam("id") String idEncoded,Principal p,HttpServletResponse response) throws IOException {
			
			System.out.println("imagen solicitada="+idEncoded);
			
			 response.setContentType("text/html");
		     response.setCharacterEncoding("ISO-8859-1");
		    
		     LocalFileManager mLocalFileManager = LocalFileManager.get();
		     
		     mLocalFileManager.copyImageBackgroundData(idEncoded, response.getOutputStream());
			//	mv.addObject("image",);
		
			//return mv;
		}
		
		
		
		//ADDING NEW YOUTUBE_LINK
				@RequestMapping(value = "add_youtube_link/{id}/{youtube_link}", method = RequestMethod.GET)
				public ModelAndView add_youtube_link(@PathVariable("id") long id,@PathVariable("youtube_link") String youtube_link, Principal p,HttpServletResponse response) throws IOException {
					System.out.println("entre en add_youtube_link!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					if(p.getName().equals("admin"))
					{
					//Descoding idLocal and idImage from idEncoded: Example: idLocal-idImage, 33-1
					
					 response.setContentType("text/html");
				     response.setCharacterEncoding("ISO-8859-1");
			 
				     LocalFileManager mLocalFileManager = LocalFileManager.get();
				     Postosaude g = PostosSaude.findById(id);
				     //decoding LINK: replace(/\//g,"_").replace(/\./g,",").replace(/\?/g,"-")
				     youtube_link = youtube_link.replace("_", "/").replace(",", ".").replace("-", "?");
				     
				     System.out.println("link added="+youtube_link);
				     g.add_youtube_link(youtube_link);
				     PostosSaude.save(g);
				     
				     ModelAndView mModelAndView= new ModelAndView("media_admin");
						
				     	Images image = new Images();
						mModelAndView.addObject("Images",image);
						mModelAndView.addObject("id_local",id);
						mModelAndView.addObject("images_path",mLocalFileManager.getImagesFromLocal(PostosSaude.findById(id)));
						mModelAndView.addObject("youtube_links",PostosSaude.findById(id).getYoutubeLinkVideos());
						
						return mModelAndView;
					}
					else 
					{
						return (new ModelAndView("media_admin"));
					}
				}	
		//deleting an Image
		@RequestMapping(value = "delete_image/"/*{idLocal}/{idImage}"*/, method = RequestMethod.POST)
		public ModelAndView deleteImage(@RequestParam("id") String idEncoded,Principal p,HttpServletResponse response) throws IOException {
			System.out.println("entre en deleteImage");
			if(p.getName().equals("admin"))
			{
			//Descoding idLocal and idImage from idEncoded: Example: idLocal-idImage, 33-1
			long idLocal = 0;
			int idImage = 0;
			System.out.println("idEncoded="+idEncoded);
			String aux = "";
			boolean idLocalGotten = false;
			for(int i = 0; i < idEncoded.length() ; i++)
			{
				if(!idEncoded.substring(i,i+1).equals("-"))
				{
					aux += idEncoded.substring(i,i+1) ;
				}
				else
				{
					if(!idLocalGotten)
					{
						idLocal = Integer.parseInt(aux);
						aux="";
						idLocalGotten = true;
					}
				}
			}
			
			idImage = Integer.parseInt(aux);
			
			 response.setContentType("text/html");
		     response.setCharacterEncoding("ISO-8859-1");
		    
		     LocalFileManager mLocalFileManager = LocalFileManager.get();
		     Postosaude g = PostosSaude.findById(idLocal);
		     
		     mLocalFileManager.deleteImageFromLocal(g,idImage);
		     ModelAndView mModelAndView= new ModelAndView("media_admin");
				
		     	Images image = new Images();
				mModelAndView.addObject("Images",image);
				mModelAndView.addObject("id_local",idLocal);
				mModelAndView.addObject("images_path",mLocalFileManager.getImagesFromLocal(PostosSaude.findById(idLocal)));
				mModelAndView.addObject("youtube_links",PostosSaude.findById(idLocal).getYoutubeLinkVideos());
				return mModelAndView;
			}
			else 
			{
				return (new ModelAndView("media_admin"));
			}
		}	
		
		//GET LOCAL IMAGE
		@RequestMapping(value = LOCAL_PHOTO_VAR_PATH, method = RequestMethod.GET)
		public void getGiftData(@PathVariable("cliente") String cliente,@PathVariable("projeto") String projeto,@PathVariable("LocalId") long id,@PathVariable("PictureId") int PictureId,
				HttpServletResponse response) throws IOException {

			LocalFileManager mLocalFileManager = LocalFileManager.get();
			
			if (PostosSaude.exists(id)) {

				Postosaude g = PostosSaude.findById(id);
				if (mLocalFileManager.hasLocalData(g,PictureId)) {
					mLocalFileManager.copyGiftData(g, PictureId,response.getOutputStream());
				} else {
				
					response.sendError(HttpServletResponse.SC_NOT_FOUND);
				}
			} else {
		
				response.sendError(HttpServletResponse.SC_NOT_FOUND);
			}

			// return null;
		}
	
	@RequestMapping("logout")  
	public String logout(HttpSession session) {  
	    session.invalidate();  
	    return "redirect:login";  
	}  
	//GET UM PROJETO COMPLETO:
	@RequestMapping(value = "getproject/{nomeCliente}/{nomeProjeto}", method = RequestMethod.GET)
	public @ResponseBody String getOneCompleteProject(@PathVariable("nomeCliente") String nomeCliente,@PathVariable("nomeProjeto") String nomeProjeto,
			HttpServletResponse response, Principal p) throws IOException {
		
		String json="error";
		nomeCliente = nomeCliente.replace("_", " ");
		nomeProjeto = nomeProjeto.replace("_", " ");
		System.out.println("Chegou="+nomeCliente+" quiere el proyecto="+nomeProjeto);
		if(p.getName().equals(nomeCliente))
		{
			System.out.println("!!!!----------- los nombres cliente y principal coinciden");
			List<Project> projetos = Lists.newArrayList(Projects.findAll());
			for(int i=0;i<projetos.size();i++)
			{
				
				if(projetos.get(i).getProject_name().equals(nomeProjeto))
				{
					System.out.println("!!!!----------- los nombres de los proyectos coinciden");
					
							
							ObjectMapper mapper = new ObjectMapper();
						     
							try {
								
									json = mapper.writeValueAsString(projetos.get(i));
									System.out.println("!!!!----------- JSON envian:"+json);
									System.out.println("!!!----- LAS RESPUESTAS dentro de Project="+projetos.get(i).getLista().size());
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						
				}
			}
		}
		
		return json;
	}
	
	//GET UM PROJETO COMPLETO:
		@RequestMapping(value = "getlistmap/{nomeCliente}/{project_id}/{respostas}", method = RequestMethod.GET)
		public @ResponseBody String getListMapfromOneProject(@PathVariable("nomeCliente") String nomeCliente,@PathVariable("project_id") long project_id,
				@PathVariable("respostas") String respostas,Principal p,HttpServletResponse response ) throws IOException {
			
			
			List<Postosaude> listafinal = new ArrayList<Postosaude>();
			
			// USER CONTROL SECURETY
			if(p.getName().equals(nomeCliente))
			{
				//FILTER DESCODIFICATION
				List<String> respostasFiltro = FilterCodification(respostas);
				
				//FIND FIRST ANSWER
				List<Postosaude> listapostossaudeTotal = Lists.newArrayList(PostosSaude.findAll());
				//FILTERING BY ID_PROJECT
				List<Postosaude> listaProjetoPostosaude = new ArrayList<Postosaude>();
				for(int i=0;i<listapostossaudeTotal.size();i++)
				{
					
					if(listapostossaudeTotal.get(i).getProject_id() == project_id)
					{
						listaProjetoPostosaude.add(listapostossaudeTotal.get(i));
					}
					
				}
				  //SORTING FOR THE LOCALS_ID
				  Collections.sort(listaProjetoPostosaude, new Comparator<Postosaude>() { 
				        @Override public int compare(Postosaude p1, Postosaude p2) { 
				            return (int) (p1.getId()- p2.getId()); 
				        } 
				 
				    });
				  
				  
				List<List<Postosaude>> listaDeListasPerguntas = new ArrayList<List<Postosaude>>();
				
				for( int a=0;a<respostasFiltro.size();a++)
				{
					List<String> respostasParaPerguntaX= new ArrayList<String>();
					List<Postosaude> listapostossaudeFilter = new ArrayList<Postosaude>();
					String aux="";
					for( int i=0;i<respostasFiltro.get(a).length();i++)
					{
						if(!respostasFiltro.get(a).substring(i, i+1).equals("_"))
						{
							aux +=  respostasFiltro.get(a).substring(i, i+1);
											}
						else
						{
							respostasParaPerguntaX.add(aux);
							System.out.println("Pergunta="+a+" y respuestas recibidas="+aux);
							aux = "";
						}
					}
					
					
					for (int i=0; i<listaProjetoPostosaude.size();i++)
					{
						for( int j=0;j<respostasParaPerguntaX.size();j++)
						{
					
							if(respostasParaPerguntaX.get(j).equals("0"))
							{
								listapostossaudeFilter = listaProjetoPostosaude;
							}
							else
							{
								if(listaProjetoPostosaude.get(i).getLista_respostas().get(a).equals(respostasParaPerguntaX.get(j)))
									{
										boolean ItWasAddedBefore =false;
										for( int k=0;k<listapostossaudeFilter.size();k++)
										{
											if(listapostossaudeFilter.get(k).getId() == listaProjetoPostosaude.get(i).getId())
											{
												ItWasAddedBefore = true;
											}
										}
										if(!ItWasAddedBefore)
										{
											listapostossaudeFilter.add(listaProjetoPostosaude.get(i));
											
										}
									}
							}
						}
					}
					listaDeListasPerguntas.add(listapostossaudeFilter);
			}
				System.out.println("listaDeListasPergunrtas sixze()="+listaDeListasPerguntas.size());
					
				listafinal =listaDeListasPerguntas.get(0);
				for( int h=0;h<listaDeListasPerguntas.size();h++)
				{
					listafinal.retainAll(listaDeListasPerguntas.get(h));
				}
				System.out.println("listafinal size="+listafinal.size());
				
				for( int y=0;y<listafinal.size();y++)
				{
					System.out.println("listafinal("+y+")="+listafinal.get(y));
				}
				//CREAMOS EL JSON!!!!!!
				String	json="error";
				 try {
					 ObjectMapper mapper = new ObjectMapper();
					 json = mapper.writeValueAsString(listafinal);
					 System.out.println("!!!!----------- JSON envian:"+json);
				} catch (Exception e) {
					e.printStackTrace();
					}
				 return json;
			}
			else
			{
				return "adios";
			}
			
		}
		
		//ADMIN GET ALL ANSWER FROM A PROJECT
		//GET UM PROJETO COMPLETO:
		@RequestMapping(value = "getanswersfromaproject/{nomeCliente}/{idProjeto}", method = RequestMethod.GET)
		public @ResponseBody String getAllAnswerFromAProject(@PathVariable("nomeCliente") String nomeCliente,@PathVariable("idProjeto") long idProjeto,
				Principal p,HttpServletResponse response ) throws IOException {
			
			System.out.println("recibiendo=getanswersfromaproject/admin/"+idProjeto);
			
			// USER CONTROL SECURETY
			if(p.getName().equals(nomeCliente) || p.getName().equals("admin"))
			{
			
				List<Postosaude> listapostossaudeTotal = Lists.newArrayList(PostosSaude.findAll());
				List<Postosaude> listaProjetoPostosaude = new ArrayList<Postosaude>();
				for(int i=0;i<listapostossaudeTotal.size();i++)
				{
					
					if(listapostossaudeTotal.get(i).getProject_id() == idProjeto)
					{
						listaProjetoPostosaude.add(listapostossaudeTotal.get(i));
					}
					
				}
				  //SORTING FOR THE LOCALS_ID
				  Collections.sort(listaProjetoPostosaude, new Comparator<Postosaude>() { 
				        @Override public int compare(Postosaude p1, Postosaude p2) { 
				            return (int) (p1.getId()- p2.getId()); 
				        } 
				 
				    });
				//CREAMOS EL JSON!!!!!!
				String	json="error";
				 try {
					 ObjectMapper mapper = new ObjectMapper();
					 json = mapper.writeValueAsString(listaProjetoPostosaude);
					 System.out.println("!!!!----------- JSON envian:"+json);
					
				} catch (Exception e) {
					e.printStackTrace();
					}
				 return json;
			}
			else
			{
				return "adios";
			}
			
		}
	
	
	//get clientes!!
	@RequestMapping(value = "getclients", method = RequestMethod.GET)
	public @ResponseBody String getClientes(Principal p,HttpServletResponse response) {
	
	
	     String json = "";
	     ObjectMapper mapper = new ObjectMapper();
	     List<Cliente> clientes = Lists.newArrayList(Clientes.findAll());
	     
	     for(int i=0;i<clientes.size();i++)
	     {
	    	 
		    		 try {json = mapper.writeValueAsString(clientes);
							System.out.println("!!!!----------- JSON envian:"+json);
					} catch (Exception e) {
						e.printStackTrace();
						}
		 }
	     return json;
	}
	
	
	
	
	//GET MAPAS DO CLIENTE!!!!!
	//get postos!!
		@RequestMapping(value = "getListaMapas", method = RequestMethod.GET)
		public @ResponseBody String getMapasCliente(Principal p,HttpServletResponse response) {
		
		;
		     String json = "";
		     ObjectMapper mapper = new ObjectMapper();
		     List<Project> projetos = Lists.newArrayList(Projects.findAll());
		     for(int i=0;i<projetos.size();i++)
		     {
		    	 for(int j=0;j<projetos.get(i).getClients_with_project().size();j++)
		    	 {
			    	 if(projetos.get(i).getClients_with_project().get(j).equals(p.getName()))
			    	 {
			    		 try {
								
								json = mapper.writeValueAsString(projetos.get(i));
								System.out.println("!!!!----------- JSON envian:"+json);
						} catch (Exception e) {
							e.printStackTrace();
							}
			    	 }
		     }
		     
				
				}
			
			return json;
		}
		
		
		@RequestMapping(value = "getListaMapas/{loggin_name}", method = RequestMethod.GET)
		public @ResponseBody String getMapasCliente(@PathVariable("loggin_name") String loggin_name,Principal p,HttpServletResponse response) {
		
		;
		     String json = "";
		     ObjectMapper mapper = new ObjectMapper();
		     List<Project> projetos = Lists.newArrayList(Projects.findAll());
		     for(int i=0;i<projetos.size();i++)
		     {
		    	 for(int j=0;j<projetos.get(i).getClients_with_project().size();j++)
		    	 {
			    	 if(projetos.get(i).getClients_with_project().get(j).equals(loggin_name))
			    	 {
			    		 try {
								
								json = mapper.writeValueAsString(projetos.get(i));
								System.out.println("!!!!----------- JSON envian:"+json);
						} catch (Exception e) {
							e.printStackTrace();
							}
			    	 }
		     }
		    }
			
			return json;
		}
		
		@RequestMapping(value = "update_mapa_credito_form", method=RequestMethod.GET)
		public ModelAndView  update_mapa_credito_form(Principal p) {

			ModelAndView mv = new ModelAndView("update_mapa_credito_form");
			Images image = new Images();
			 mv.addObject("Images",image);
			return mv;
		} 
			
		
		
	@RequestMapping(value = "login", method=RequestMethod.GET)
	public /*ModelAndView*/ void pageLogin(Model model) {

	} 
	@RequestMapping(value = "update_gps_form", method=RequestMethod.GET)
	public ModelAndView mudar_position_gps(Principal p) 
	{
		ModelAndView mv = null;
	
		if(p.getName().equals("credito") || p.getName().equals("admin"))
		{
			/*List<Credito_clientes_banco> lista_clientes = Lists.newArrayList(CreditoClientesBanco.findAll());
			List<Credito_clientes_banco> lista_clientes_final = new ArrayList<Credito_clientes_banco>();
			for(Credito_clientes_banco ccb : lista_clientes)
			{
				
				if(!ccb.isGps_position_updated())
				{
					lista_clientes_final.add(ccb);
				}
			}*/
			mv = new ModelAndView("update_gps_form");
			mv.addObject("mudar_position_gps",new Mudar_position_gps());
			mv.addObject("clientes",CreditoClientesBanco.findByGpspositionupdated(false));
			return mv;
		}
		return mv;
	}
	
	@RequestMapping(value = "update_one_gps_form/{id}", method=RequestMethod.GET)
	public ModelAndView update_one_gps_form(@PathVariable("id") String id,Principal p) 
	{
		ModelAndView mv = null;
		System.out.println("dentro del update_one_gps_form id  = "+id);
		if(p.getName().equals("credito") || p.getName().equals("admin"))
		{
			Mudar_position_gps mudar  = new Mudar_position_gps();
			mudar.setId(CreditoClientesBanco.findById( Long.parseLong(id, 10)).getCodigocliente()+"");
System.out.println("mudar.getId() = "+mudar.getId());
		//	List<Credito_clientes_banco> lista = Lists.newArrayList(CreditoClientesBanco.findAll());
		//	Credito_clientes_banco ccb1 = null;
			
			/*for(Credito_clientes_banco ccb : lista)
			{
				if(ccb.getCodigo_cliente().get(0).equals(id))
				{
					ccb1 = ccb;
					mudar.setId(ccb.getCodigo_cliente().get(0));
				}
			}*/
			
			mv = new ModelAndView("update_one_gps_form");
			mv.addObject("mudar_position_gps",mudar);
			System.out.println("adding Objects and returning in UPDATE_ONE_GPS_FORM to -> update_one_gps_form/{id}");
			mv.addObject("cliente",CreditoClientesBanco.findById( Long.parseLong(id, 10) )  );
			return mv;
		}
		return mv;
	}
	public List<Alerta> getAlertas()
	{
		List<Alerta> alertas = new ArrayList<Alerta>();
		
		//ALERTAS DE PAGAMENTO!
		List<LinhaNegPagamento> llnp = mLinhaNegPagamentoRepository.findByAlertaativa(true);
		for(LinhaNegPagamento lnp : llnp)
		{
			Alerta alerta = new Alerta();
			alerta.setDataalerta(lnp.getDataalerta());
			alerta.setIdcliente(mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcliente()+"");
			alerta.setNomecliente(CreditoClientesBanco.findById(mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcliente()).getNome_cliente());
			alerta.setTipoalerta("PAGAMENTO");
			
			alertas.add(alerta);
		}
		//ALERTAS DE LIGAR!
		List<LinhaNegLigar> llnl = mLinhaNegLigarRepository.findByAlertaativa(true);
		for(LinhaNegLigar lnp : llnl)
		{
			Alerta alerta = new Alerta();
			alerta.setDataalerta(lnp.getDataalerta());
			alerta.setIdcliente(mNegociacaoLigarRepository.findById(lnp.getIdnegociacao()).getIdcliente()+"");
			alerta.setNomecliente(CreditoClientesBanco.findById(mNegociacaoLigarRepository.findById(lnp.getIdnegociacao()).getIdcliente()).getNome_cliente());
			alerta.setTipoalerta("LIGAR");
			alertas.add(alerta);
		}
		//ALERTAS DE LIGAR!
		List<LinhaNegVisita> llnv = mLinhaNegVisitaRepository.findByAlertaativa(true);
		for(LinhaNegVisita lnp : llnv)
		{
			Alerta alerta = new Alerta();
			alerta.setDataalerta(lnp.getDataalerta());
			alerta.setIdcliente(mNegociacaoVisitaRepository.findById(lnp.getIdnegociacao()).getIdcliente()+"");
			alerta.setNomecliente(CreditoClientesBanco.findById(mNegociacaoVisitaRepository.findById(lnp.getIdnegociacao()).getIdcliente()).getNome_cliente());
			alerta.setTipoalerta("VISITA");
			alertas.add(alerta);
		}
		System.out.println("alertas.size() = "+alertas.size());
		
		
		return alertas;
	}
	//INDEX
	@RequestMapping(value = "index", method=RequestMethod.GET)
	public ModelAndView index(Principal p) {
		
		System.out.println("Cliente reconhecido = "+p.getName());
		if(p.getName().equals("credito"))
		{
		ObjectMapper mapper = new ObjectMapper();
			ModelAndView mv = new ModelAndView("mapa_credito");
			//try{
				//mv.addObject("clientes_banco",mapper.writeValueAsString(CreditoClientesBanco.findAll()));
				//mv.addObject("clientes_banco",Lists.newArrayList(CreditoClientesBanco.findAll()));
				mv.addObject("clientes_banco",CreditoClientesBanco.findAll());
			//} catch (Exception e) {
			//e.printStackTrace();}
			mv.addObject("seguimento_cliente", new seguimento_cliente());
			atualizarAlertas();
			mv.addObject("alertas",getAlertas());
			mv.addObject("Buscaendereco", new Buscaendereco());
			/*ModelAndView mv = new ModelAndView("update_mapa_credito_form");
			Images image = new Images();
			 mv.addObject("Images",image);*/
			return mv;
		}
		else if(p.getName().equals("admin"))
		{
			ModelAndView mv = new ModelAndView("correct");
			return mv;
		}
		else {
			ModelAndView mv = new ModelAndView("index");
			List<Cliente> clientes= Lists.newArrayList(Clientes.findAll());
			
			for(int i=0;i<clientes.size();i++)
			{
				if(p.getName() .equals(clientes.get(i).getLogginname()))
				{
					mv.addObject("cliente", Clientes.findById(clientes.get(i).getId()));
				}
			}
			
			return mv;
		}
	}
	//POST 1 CLIENTE
	@RequestMapping(value = "addCliente")
	public ModelAndView addCliente() 
	{
		boolean addNewCliente = true;
		List<Cliente> clientes= Lists.newArrayList(Clientes.findAll());
		
		if(clientes !=null )
		{
			for(int i=0;i<clientes.size();i++)
			{
				if(clientes.get(i).getLogginname() .equals("coursera"))
				{
					addNewCliente = false;
				}
			}
		}
		if(addNewCliente)
		{
			Cliente cliente= new Cliente("coursera","IPLANFOR - Prefeitura de Fortaleza", new Date());
			Clientes.save(cliente);
			System.out.println("Se addicionou new cliente="+cliente.getComplete_name());
		}
			
			ModelAndView mModelAndView= new ModelAndView("test");
			return mModelAndView;
	}
	//POST 1 POSTOS SAUDE PROJECT
		@RequestMapping(value = "addProjectPostoSaude", method = RequestMethod.GET)
		public ModelAndView addProject() 
		{
			boolean addNewProject = true;
			List<Cliente> clientes= Lists.newArrayList(Clientes.findAll());
			List<Project> projects =  Lists.newArrayList(Projects.findAll()); 
			Cliente cliente = null;
			for(int i=0;i<clientes.size();i++)
			{
				if(clientes.get(i).getLogginname().equals("coursera"))
				{
					cliente = clientes.get(i);
				}
			}
			for(int i=0;i<projects.size();i++)
			{
				if(projects.get(i).getProject_name().equals("Indicadores de Gestao para Postos de Saude Municipais de Fortaleza"))
				{
					addNewProject = false;
				}
			}
			if(addNewProject)
			{
				Date d = new Date();
				List<parametros> listaparametros = new ArrayList<parametros>();
				listaparametros.add(new parametros(PostoSaudeUtility.Pergunta1,PostoSaudeUtility.ListaRespostas1));
				listaparametros.add(new parametros(PostoSaudeUtility.Pergunta2,PostoSaudeUtility.ListaRespostas2));
				listaparametros.add(new parametros(PostoSaudeUtility.Pergunta3,PostoSaudeUtility.ListaRespostas3));
				parametros.save(listaparametros);
				Project mProject =new Project("Indicadores de Gestao para Postos de Saude Municipais de Fortaleza",new Date(),"Postosaude",listaparametros);
				mProject.addClient_to_the_project(cliente.getLogginname());
				Projects.save(mProject);
		//		cliente.addProject(mProject);
				Clientes.save(cliente);
			}
				
				ModelAndView mModelAndView= new ModelAndView("test");
				return mModelAndView;
		}
		//POST 1 FORM TESTE
				@RequestMapping(value = "formteste", method = RequestMethod.POST)
				public ModelAndView formteste(@ModelAttribute survey_question msurvey_question, Model model) 
				{
					System.out.println("survey_question is null?"+(msurvey_question==null));
					System.out.println("survey_question.pergunta"+msurvey_question.getPergunta());
					for(int i=0;i<msurvey_question.getRrespostas().size();i++)
					{
						System.out.println("msurvey_question.getRrespostas().get(i)="+msurvey_question.getRrespostas().get(i));
					}
					
					
						
						ModelAndView mModelAndView= new ModelAndView("test");
						return mModelAndView;
				}
				//EDITING SURVEY
				@RequestMapping(value = "editingSurvey/{id_project}", method = RequestMethod.GET)
				public ModelAndView showMessage(@PathVariable("id_project") long id_project,Principal p,HttpServletResponse response) {
				
					
					ModelAndView mv = new ModelAndView("fill_survey");
					  response.setContentType("text/html");
				     response.setCharacterEncoding("ISO-8859-1");
				     
				     survey_question msurvey_question = new survey_question();
				   
						mv.addObject("survey_question",msurvey_question);
						mv.addObject("project",Projects.findById(id_project));
						mv.addObject("surveys_done",Projects.findById(id_project).getAllSurveyQuestions());
				
					return mv;
				}
				
				//POST 1 SURVEY FOR A PROJECT!!!
				@RequestMapping(value = "mudar_position_gps", method = RequestMethod.POST)
				public ModelAndView mudar_position_gps(@ModelAttribute Mudar_position_gps mMudar_position_gps) 
				{
					
						
						System.out.println("mMudar id="+mMudar_position_gps.getId());
						System.out.println("mMudar Latitude="+mMudar_position_gps.getLatitude());
						System.out.println("mMudar long="+mMudar_position_gps.getLongitude());
						System.out.println("mMudar endereco="+mMudar_position_gps.getEnde());
						
						Credito_clientes_banco ccb = CreditoClientesBanco.findByCodigocliente(Long.parseLong(mMudar_position_gps.getId()));	
								ccb.setLatitude(Double.parseDouble(mMudar_position_gps.getLatitude()));
								ccb.setLongitude(Double.parseDouble(mMudar_position_gps.getLongitude()));
								ccb.setEndereco(mMudar_position_gps.getEnde());
								ccb.setGpspositionupdated(true);
								CreditoClientesBanco.save(ccb);
						
						ModelAndView mModelAndView= new ModelAndView("mapa_credito");
						mModelAndView.addObject("clientes_banco",Lists.newArrayList(CreditoClientesBanco.findAll()));
						mModelAndView.addObject("seguimento_cliente", new seguimento_cliente());
						mModelAndView.addObject("Buscaendereco", new Buscaendereco());
						mModelAndView.addObject("alertas",getAlertas());
						return mModelAndView;
				}
				
		//DOWNLOAD RELATORIO DE CREDITO!
		@RequestMapping(value = "downloadrelatorio", method = RequestMethod.GET)
		public void downloadRelatorio(@RequestParam("datainicio")String dataInicio,@RequestParam("datafinal")String dataFinal,HttpServletResponse response) throws ParseException
		{
			
			 String csvFileName = "Relatorio_"+dataInicio+"_"+dataFinal+".csv";
			 response.setContentType("text/csv");
			 
		        // creates mock data
		        String headerKey = "Content-Disposition";
		        String headerValue = String.format("attachment; filename=\"%s\"",
		                csvFileName);
		        response.setHeader(headerKey, headerValue);
		        
		        
		        //CALCULANDO DATAS
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        Date date1 = null,date2 = null;
				try {
					date1 = sdf.parse(dataInicio);
					System.out.println("dataInicio = "+date1.getTime());
					date2 = sdf.parse(dataFinal);
					System.out.println("dataFinal = "+date2.getTime());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// uses the Super CSV API to generate CSV data from the model data
		        ICsvBeanWriter csvWriter;
				try {
					csvWriter = new CsvBeanWriter(response.getWriter(),
					        CsvPreference.STANDARD_PREFERENCE);
					//Craindo data de Hoje
					Date datahoje = new Date();
				//Header 1
					String header1="Relatório de Pagamentos criado no "+sdf.format(datahoje)+" por PalmasLab.";
				//Header 2
				String header2="Este Relatório contem os pagamentos registrados na Plataforma desde o dia "+dataInicio+" até "+dataFinal;
				//Header 3
		        String[] header3 = { "Data do Pagamento", "Nome do Cliente", "Código do Cliente", "Código do Contrato",
		                "Valor Pago", "Situação do Contrato","Endereço" };
		        String[] header4 = {"data","nomecliente","codigocliente","codigocontrato","valorpago","situacao","endereco"};
		        csvWriter.writeHeader(header1);
		        csvWriter.writeHeader("");
		        csvWriter.writeHeader(header2);
		        csvWriter.writeHeader("");
		        csvWriter.writeHeader(header3);
		        
		     
		        List<LinhaNegPagamento> ListapagamentosFinal = new ArrayList<LinhaNegPagamento>();
		        List<RelatorioPagamento> listaRelatorioPagamento = new ArrayList<RelatorioPagamento>();
		        boolean continuar = true;
		        int i =0;
		        while(continuar)
		        {
		        	try {
		        	if(sdf.parse(sumarRestarDiasFecha(dataInicio,i)).getTime()<(date2.getTime()))
		        	{
		        		ListapagamentosFinal.addAll(mLinhaNegPagamentoRepository.findByDataalertaAndChecked(sumarRestarDiasFecha(dataInicio,i), true));
		        		i++;
		        		System.out.println("Vuelta "+i+": ListapagamentosFinal.size() = "+ListapagamentosFinal.size());
		        	}
		        	else
		        	{
		        		continuar = false;
		        	}
		        	} catch (ParseException e) {e.printStackTrace();
					}
		        }
		     
		        for (LinhaNegPagamento lnp : ListapagamentosFinal)
		        {
		        	System.out.println("lnp FINAL!, lnp.getDataalerta() = "+lnp.getDataalerta()+" nomecliente = "+CreditoClientesBanco.findById(mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcliente()).getNome_cliente());
		        	listaRelatorioPagamento.add( new RelatorioPagamento(lnp.getDataalerta(),CreditoClientesBanco.findById(mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcliente()).getNome_cliente(),mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcliente()+"",mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcontrato(),"PERDA",lnp.getValorrecebido(),CreditoClientesBanco.findById(mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcliente()).getEndereco()+" "+CreditoClientesBanco.findById(mNegociacaoPagamentoRepository.findById(lnp.getIdnegociacao()).getIdcliente()).getBairro()));
		        }
		        double contagem = 0.0;
		        for( RelatorioPagamento rp : listaRelatorioPagamento)
		        {
		        csvWriter.write(rp, header4);
		        contagem += Double.parseDouble(rp.getValorpago());
		        }
		        String footer = "Valor Total Recebido entre o dia "+dataInicio+" e o dia "+dataFinal+" = "+contagem+" R$";
		        csvWriter.writeComment(footer);
		        csvWriter.close();
		        
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}
				
		//POST 1 SURVEY FOR A PROJECT!!!
		@RequestMapping(value = "addSurveyQuestion/", method = RequestMethod.POST)
		public ModelAndView addSurveyQuestion(@RequestParam("id") long id_project,@ModelAttribute survey_question msurvey_question, Model model) 
		{
			boolean addNewSurveyQuestion = true;
			
			if(addNewSurveyQuestion)
			{
				Surveys.save(msurvey_question);
				System.out.println("Acabo de addicionar un questionSurvey, creandose con la Id="+msurvey_question.getId());
				Projects.findById(id_project).addSurveyQuestion(msurvey_question);
				Projects.save(Projects.findById(id_project));
			}
			survey_question NewmSurvey_question = new survey_question();
				ModelAndView mModelAndView= new ModelAndView("fill_survey");
				mModelAndView.addObject("survey_question",NewmSurvey_question);
	     		mModelAndView.addObject("project",Projects.findById(id_project));
				mModelAndView.addObject("surveys_done",Projects.findById(id_project).getAllSurveyQuestions());
				return mModelAndView;
		}
		//POST 1 SURVEY FOR A PROJECT!!!
				@RequestMapping(value = "EditOneSurveyQuestion/{id_project}/{id_survey}", method = RequestMethod.GET)
				public ModelAndView EditOneSurveyQuestion(@PathVariable("id_project")long id_project,@PathVariable("id_survey") long id_survey,@ModelAttribute survey_question msurvey_question, Model model) 
				{
						ModelAndView mModelAndView= new ModelAndView("editing_one_survey_question");
				        mModelAndView.addObject("project",Projects.findById(id_project));
			     		for(int i=0; i<Projects.findById(id_project).getAllSurveyQuestions().size();i++)
			     		{
			     			if(Projects.findById(id_project).getAllSurveyQuestions().get(i).getId() == id_survey)
			     			{
			     				mModelAndView.addObject("survey_done",Projects.findById(id_project).getAllSurveyQuestions().get(i));
			     				mModelAndView.addObject("id_survey",id_survey);
			     			}
			     		}
						
					
						
						return mModelAndView;
				}
				//SET!!!!! POST 1 SURVEY(EDITED) FOR A PROJECT!!!
				@RequestMapping(value = "setOneSurveyQuestion/{id_project}/{id_survey}", method = RequestMethod.POST)
				public ModelAndView SetOneSurveyQuestion(@PathVariable("id_project")long id_project,@PathVariable("id_survey") long id_survey,@ModelAttribute survey_question msurvey_question, Model model) 
				{
						ModelAndView mv = new ModelAndView("fill_survey");
						
						
						System.out.println("este es el ID del survey editado que me llega="+id_survey);
						for( int i=0;i<Lists.newArrayList(Surveys.findAll()).size();i++)
						{
							if(Lists.newArrayList(Surveys.findAll()).get(i).getId() == id_survey)
							{
								System.out.println(" como era de esperar este mSurveyQuestion existe, coincidiendo la ID="+id_survey+" con ID="+Lists.newArrayList(Surveys.findAll()).get(i).getId() );
							}
						}
						Surveys.findById(id_survey).setPergunta(msurvey_question.getPergunta());
						Surveys.findById(id_survey).setRrespostas(msurvey_question.getRrespostas());
						Surveys.save(Surveys.findById(id_survey));
						Projects.findById(id_project).setOneEditedSurvey(Surveys.findById(id_survey));
						Projects.save(Projects.findById(id_project));
						
						survey_question newsurvey_question = new survey_question();
						mv.addObject("survey_question",newsurvey_question);
						mv.addObject("project",Projects.findById(id_project));
						mv.addObject("surveys_done",Projects.findById(id_project).getAllSurveyQuestions());
					
						return mv;
				}
				//DELETEEEE!!!!! POST 1 SURVEY(EDITED) FOR A PROJECT!!!
				@RequestMapping(value = "deleteOneSurveyQuestion/{id_project}/{id_survey}", method = RequestMethod.GET)
				public ModelAndView DeleteOneSurveyQuestion(@PathVariable("id_project")long id_project,@PathVariable("id_survey") long id_survey,@ModelAttribute survey_question msurvey_question, Model model) 
				{
						ModelAndView mv = new ModelAndView("fill_survey");
						
						Projects.findById(id_project).deleteOneEditedSurvey((int) id_survey);
						Surveys.delete(Surveys.findById(id_survey));
						
						
						survey_question newsurvey_question = new survey_question();
						mv.addObject("survey_question",newsurvey_question);
						mv.addObject("project",Projects.findById(id_project));
						mv.addObject("surveys_done",Projects.findById(id_project).getAllSurveyQuestions());
					
						return mv;
				}

				//EDITING SURVEY
				@RequestMapping(value = "editingParameters/{id_project}", method = RequestMethod.GET)
				public ModelAndView editingParameters(@PathVariable("id_project") long id_project,Principal p,HttpServletResponse response) {
				
					
					ModelAndView mv = new ModelAndView("fill_parameters");
					  response.setContentType("text/html");
				     response.setCharacterEncoding("ISO-8859-1");
				     
				     parametros mparametros = new parametros();
				   
						mv.addObject("parameter_question",mparametros);
						mv.addObject("project",Projects.findById(id_project));
						mv.addObject("parameters_done",Projects.findById(id_project).getAllParametersQuestions());
				
					return mv;
				}
		//POST 1 SURVEY FOR A PROJECT!!!
		@RequestMapping(value = "addParametersQuestion/", method = RequestMethod.POST)
		public ModelAndView addParameterQuestion(@RequestParam("id") long id_project,@ModelAttribute parametros mparametros, Model model) 
		{
			boolean addNewParametroQuestion = true;
			
			if(addNewParametroQuestion)
			{
				parametros.save(mparametros);
				System.out.println("Acabo de addicionar un questionPmparametros, creandose con la Id="+mparametros.getId());
				Projects.findById(id_project).addParametrosQuestion(mparametros);;
				Projects.save(Projects.findById(id_project));
			}
			parametros new_parametros = new parametros();
				ModelAndView mModelAndView= new ModelAndView("fill_parameters");
				mModelAndView.addObject("parameter_question",new_parametros);
	     		mModelAndView.addObject("project",Projects.findById(id_project));
				mModelAndView.addObject("parameters_done",Projects.findById(id_project).getAllParametersQuestions());
				return mModelAndView;
		}
		//POST 1 SURVEY FOR A PROJECT!!!
				@RequestMapping(value = "EditOneParameterQuestion/{id_project}/{id_survey}", method = RequestMethod.GET)
				public ModelAndView EditOneParameterQuestion(@PathVariable("id_project")long id_project,@PathVariable("id_survey") long id_survey,@ModelAttribute parametros mparametros, Model model) 
				{
						ModelAndView mModelAndView= new ModelAndView("editing_one_parameter_question");
				        mModelAndView.addObject("project",Projects.findById(id_project));
			     		for(int i=0; i<Projects.findById(id_project).getAllParametersQuestions().size();i++)
			     		{
			     			if(Projects.findById(id_project).getAllParametersQuestions().get(i).getId() == id_survey)
			     			{
			     				mModelAndView.addObject("parameter_done",Projects.findById(id_project).getAllParametersQuestions().get(i));
			     				mModelAndView.addObject("id_survey",id_survey);
			     			}
			     		}
						
					
						
						return mModelAndView;
				}
				//SET!!!!! POST 1 SURVEY(EDITED) FOR A PROJECT!!!
				@RequestMapping(value = "setOneParameterQuestion/{id_project}/{id_survey}", method = RequestMethod.POST)
				public ModelAndView SetOneParameterQuestion(@PathVariable("id_project")long id_project,@PathVariable("id_survey") long id_survey,@ModelAttribute parametros mparametros, Model model) 
				{
						ModelAndView mv = new ModelAndView("fill_parameters");
						
						
						System.out.println("este es el ID del survey editado que me llega="+id_survey);
						for( int i=0;i<Lists.newArrayList(parametros.findAll()).size();i++)
						{
							if(Lists.newArrayList(parametros.findAll()).get(i).getId() == id_survey)
							{
								System.out.println(" como era de esperar este mSurveyQuestion existe, coincidiendo la ID="+id_survey+" con ID="+Lists.newArrayList(parametros.findAll()).get(i).getId() );
							}
						}
						parametros.findById(id_survey).setPergunta(mparametros.getPergunta());
						parametros.findById(id_survey).setRrespostas(mparametros.getRrespostas());
						parametros.save(parametros.findById(id_survey));
						Projects.findById(id_project).setOneEditedParameter(parametros.findById(id_survey));
						Projects.save(Projects.findById(id_project));
						
						parametros newparametros = new parametros();
						mv.addObject("parameter_question",newparametros);
						mv.addObject("project",Projects.findById(id_project));
						mv.addObject("parameters_done",Projects.findById(id_project).getAllParametersQuestions());
					
						return mv;
				}
				//DELETEEEE!!!!! POST 1 SURVEY(EDITED) FOR A PROJECT!!!
				@RequestMapping(value = "deleteOneParameterQuestion/{id_project}/{id_survey}", method = RequestMethod.GET)
				public ModelAndView DeleteOneParameterQuestion(@PathVariable("id_project")long id_project,@PathVariable("id_survey") long id_survey,@ModelAttribute parametros mparametros, Model model) 
				{
						ModelAndView mv = new ModelAndView("fill_parameters");
						
						Projects.findById(id_project).deleteOneEditedParametro((int)id_survey);
						parametros.delete(parametros.findById(id_survey));
						
						
						parametros newparametros = new parametros();
						mv.addObject("parameter_question",newparametros);
						mv.addObject("project",Projects.findById(id_project));
						mv.addObject("parameters_done",Projects.findById(id_project).getAllParametersQuestions());
					
						return mv;
				}
				
				
		
		
		
		
	@RequestMapping(value = "add3/{project_id}", method = RequestMethod.GET)
	public ModelAndView addPostoSaude(@PathVariable("project_id") long project_id) 
	{
		System.out.println("Dentor de add3!!!; Numero Postos="+Lists.newArrayList(PostosSaude.findAll()).size());
			PostoSaudeUtility mUtility= new PostoSaudeUtility(); 
			List<String> listarespostas = new ArrayList<String>();
			listarespostas.add("1");
			listarespostas.add("1");
			listarespostas.add("1");
			Postosaude mPostoSaude1= new Postosaude("mi casa","Rua Julio Cesar 173","Benfica","Fortaleza","Ceara","Brasil",listarespostas,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude1), mPostoSaude1));
			
			
			List<String> listarespostass = new ArrayList<String>();
			listarespostass.add("2");
			listarespostass.add("2");
			listarespostass.add("2");
			Postosaude mPostoSaude2= new Postosaude("antigua casa","Rua Paulino Nogueira 283","Benfica","Fortaleza","Ceara","Brasil",listarespostass,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude2), mPostoSaude2));

			List<String> listarespostasss = new ArrayList<String>();
			listarespostasss.add("3");
			listarespostasss.add("1");
			listarespostasss.add("3");
			Postosaude mPostoSaude3= new Postosaude("Banco pau","Avenida Valparaiso 698","Conjunto Palmeiras","Fortaleza","Ceara","Brasil",listarespostasss,0,"hola que tal como te va",project_id);
			
			//ADICIONO LOS 20 QUESTIONARIOS PREENCHIDOS!
			//1- REALIZAR UN QUESTIONARIO A MANO!
			Random gerador = new Random();
			for(int i=0;i<15;i++)
			{
				Completed_survey a = new Completed_survey();
				List<String> listaresposta = new ArrayList<String>();
				listaresposta.add(""+(gerador.nextInt(3) + 1));
				listaresposta.add(""+(gerador.nextInt(3) + 1));
				listaresposta.add(""+(gerador.nextInt(3) + 1));
				a.setAnswer(listaresposta);
				CompletedSurveys.save(a);
				mPostoSaude3.addCompleted_survey(a);
			}
			
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude3), mPostoSaude3));
			System.out.println("Saliendo de Add3; Numero Postos="+Lists.newArrayList(PostosSaude.findAll()).size());
			
			
		//LAS NUEVAS
			List<String> listarespostas1 = new ArrayList<String>();
			listarespostas1.add("1");
			listarespostas1.add("1");
			listarespostas1.add("1");
			Postosaude mPostoSaude4= new Postosaude("1","Rua Jaime Leonel 74","Luciano Cavalcante","Fortaleza","Ceara","Brasil",listarespostas1,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude4), mPostoSaude4));
			
			List<String> listarespostas2 = new ArrayList<String>();
			listarespostas2.add("2");
			listarespostas2.add("2");
			listarespostas2.add("2");
			Postosaude mPostoSaude5= new Postosaude("2","Rua Thompson Bulcao 831","Luciano Cavalcante","Fortaleza","Ceara","Brasil",listarespostas2,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude5), mPostoSaude5));
			
			
			
			List<String> listarespostas4 = new ArrayList<String>();
			listarespostas4.add("3");
			listarespostas4.add("2");
			listarespostas4.add("3");
			Postosaude mPostoSaude7= new Postosaude("4","Rua Justino Cafe Neto 183","Guararapes","Fortaleza","Ceara","Brasil",listarespostas4,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude7), mPostoSaude7));
			
			List<String> listarespostas5 = new ArrayList<String>();
			listarespostas5.add("4");
			listarespostas5.add("1");
			listarespostas5.add("4");
			Postosaude mPostoSaude8= new Postosaude("5","Rua Sao Gabriel 318","Sao Joao do Tauape","Fortaleza","Ceara","Brasil",listarespostas5,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude8), mPostoSaude8));
			
		
			
			List<String> listarespostas7 = new ArrayList<String>();
			listarespostas7.add("1");
			listarespostas7.add("1");
			listarespostas7.add("2");
			Postosaude mPostoSaude10= new Postosaude("7","Rua Monsenhor Catao 1504","Meireles","Fortaleza","Ceara","Brasil",listarespostas7,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude10), mPostoSaude10));
			
			List<String> listarespostas8 = new ArrayList<String>();
			listarespostas8.add("1");
			listarespostas8.add("1");
			listarespostas8.add("3");
			Postosaude mPostoSaude11= new Postosaude("8","Rua Coronel Juca 1633","Meireles","Fortaleza","Ceara","Brasil",listarespostas8,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude11), mPostoSaude11));
			
			
			List<String> listarespostas9 = new ArrayList<String>();
			listarespostas9.add("1");
			listarespostas9.add("1");
			listarespostas9.add("4");
			Postosaude mPostoSaude12= new Postosaude("9","Rua Felicidade 129","Dionisio Torres","Fortaleza","Ceara","Brasil",listarespostas9,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude12), mPostoSaude12));
			
			List<String> listarespostas10 = new ArrayList<String>();
			listarespostas10.add("1");
			listarespostas10.add("2");
			listarespostas10.add("1");
			Postosaude mPostoSaude13= new Postosaude("10","Rua General Tertuliano Potiguara 575","Aldeota","Fortaleza","Ceara","Brasil",listarespostas10,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude13), mPostoSaude13));
			
			List<String> listarespostas11 = new ArrayList<String>();
			listarespostas11.add("1");
			listarespostas11.add("2");
			listarespostas11.add("2");
			Postosaude mPostoSaude14= new Postosaude("11","Rua Barbosa de Freitas 1710","Aldeota","Fortaleza","Ceara","Brasil",listarespostas11,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude14), mPostoSaude14));
			
			List<String> listarespostas12 = new ArrayList<String>();
			listarespostas12.add("1");
			listarespostas12.add("2");
			listarespostas12.add("3");
			Postosaude mPostoSaude15= new Postosaude("12","Rua Vicente Linhares 457","Aldeota","Fortaleza","Ceara","Brasil",listarespostas12,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude15), mPostoSaude15));
			
			List<String> listarespostas13 = new ArrayList<String>();
			listarespostas13.add("1");
			listarespostas13.add("2");
			listarespostas13.add("4");
			Postosaude mPostoSaude16= new Postosaude("13","Rua Visconde de Maua 1650","Meireles","Fortaleza","Ceara","Brasil",listarespostas13,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude16), mPostoSaude16));
			
			List<String> listarespostas14 = new ArrayList<String>();
			listarespostas14.add("2");
			listarespostas14.add("1");
			listarespostas14.add("1");
			Postosaude mPostoSaude17= new Postosaude("14","Rua Carolina Sucupira 112","Aldeota","Fortaleza","Ceara","Brasil",listarespostas14,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude17), mPostoSaude17));
			
			List<String> listarespostas15 = new ArrayList<String>();
			listarespostas15.add("2");
			listarespostas15.add("1");
			listarespostas15.add("2");
			Postosaude mPostoSaude18= new Postosaude("15","Rua Osvaldo Cruz 1643","Meireles","Fortaleza","Ceara","Brasil",listarespostas15,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude18), mPostoSaude18));
			
			List<String> listarespostas16 = new ArrayList<String>();
			listarespostas16.add("2");
			listarespostas16.add("1");
			listarespostas16.add("3");
			Postosaude mPostoSaude19= new Postosaude("16","Av Padre Antonio Tomas 135","Aldeota","Fortaleza","Ceara","Brasil",listarespostas16,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude19), mPostoSaude19));
			
			List<String> listarespostas17 = new ArrayList<String>();
			listarespostas17.add("2");
			listarespostas17.add("1");
			listarespostas17.add("4");
			Postosaude mPostoSaude20= new Postosaude("17","Rua Tiburcio Cavalcante 1562","Meireles","Fortaleza","Ceara","Brasil",listarespostas17,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude20), mPostoSaude20));
			
			List<String> listarespostas18 = new ArrayList<String>();
			listarespostas18.add("2");
			listarespostas18.add("2");
			listarespostas18.add("1");
			Postosaude mPostoSaude21= new Postosaude("18","Rua Afonso Celso 870","Aldeota","Fortaleza","Ceara","Brasil",listarespostas18,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude21), mPostoSaude21));
			
			List<String> listarespostas19 = new ArrayList<String>();
			listarespostas19.add("2");
			listarespostas19.add("2");
			listarespostas19.add("3");
			Postosaude mPostoSaude22= new Postosaude("19","Rua Nunes Valente 1440","Meireles","Fortaleza","Ceara","Brasil",listarespostas19,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude22), mPostoSaude22));
			
			List<String> listarespostas20= new ArrayList<String>();
			listarespostas20.add("2");
			listarespostas20.add("2");
			listarespostas20.add("4");
			Postosaude mPostoSaude23= new Postosaude("20","Rua Torres Camara 739","Aldeota","Fortaleza","Ceara","Brasil",listarespostas20,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude23), mPostoSaude23));
			
			List<String> listarespostas21 = new ArrayList<String>();
			listarespostas21.add("3");
			listarespostas21.add("1");
			listarespostas21.add("1");
			Postosaude mPostoSaude24= new Postosaude("21","Rua Jose Vilar 1252","Meireles","Fortaleza","Ceara","Brasil",listarespostas21,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude24), mPostoSaude24));
			
			List<String> listarespostas22 = new ArrayList<String>();
			listarespostas22.add("3");
			listarespostas22.add("1");
			listarespostas22.add("2");
			Postosaude mPostoSaude25= new Postosaude("22","Rua Silva Paulet 1069","Meireles","Fortaleza","Ceara","Brasil",listarespostas22,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude25), mPostoSaude25));
			
			List<String> listarespostas23 = new ArrayList<String>();
			listarespostas23.add("3");
			listarespostas23.add("1");
			listarespostas23.add("3");
			Postosaude mPostoSaude26= new Postosaude("23","Av Barao de Studart 893","Pici","Fortaleza","Ceara","Brasil",listarespostas23,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude26), mPostoSaude26));
			
			List<String> listarespostas24 = new ArrayList<String>();
			listarespostas24.add("3");
			listarespostas24.add("1");
			listarespostas24.add("4");
			Postosaude mPostoSaude27= new Postosaude("24","Rua Costa Barros 1862","Aldeota","Fortaleza","Ceara","Brasil",listarespostas24,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude27), mPostoSaude27));
			
			List<String> listarespostas25 = new ArrayList<String>();
			listarespostas25.add("3");
			listarespostas25.add("2");
			listarespostas25.add("1");
			Postosaude mPostoSaude28= new Postosaude("25","Rua Dr Jose Lourenco 702","Joaquim Tavora","Fortaleza","Ceara","Brasil",listarespostas25,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude28), mPostoSaude28));
			
			List<String> listarespostas26 = new ArrayList<String>();
			listarespostas26.add("3");
			listarespostas26.add("2");
			listarespostas26.add("2");
			Postosaude mPostoSaude29= new Postosaude("26","Rua Pereira Filgueiras 1681","Centro","Fortaleza","Ceara","Brasil",listarespostas26,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude29), mPostoSaude29));
			
			List<String> listarespostas27 = new ArrayList<String>();
			listarespostas27.add("3");
			listarespostas27.add("2");
			listarespostas27.add("3");
			Postosaude mPostoSaude30= new Postosaude("27","Av Rui Barbosa 700","Meireles","Fortaleza","Ceara","Brasil",listarespostas27,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude30), mPostoSaude30));
			
			List<String> listarespostas28 = new ArrayList<String>();
			listarespostas28.add("3");
			listarespostas28.add("2");
			listarespostas28.add("4");
			Postosaude mPostoSaude31= new Postosaude("28","Rua Monsenhor Bruno 640","Meireles","Fortaleza","Ceara","Brasil",listarespostas28,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude31), mPostoSaude31));
			
			List<String> listarespostas29 = new ArrayList<String>();
			listarespostas29.add("4");
			listarespostas29.add("1");
			listarespostas29.add("1");
			Postosaude mPostoSaude32= new Postosaude("29","Rua Carlos Vasconcelos 532","Meireles","Fortaleza","Ceara","Brasil",listarespostas29,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude32), mPostoSaude32));
			
			List<String> listarespostas30 = new ArrayList<String>();
			listarespostas30.add("4");
			listarespostas30.add("1");
			listarespostas30.add("2");
			Postosaude mPostoSaude33= new Postosaude("30","Rua Barao de Aracati 452","Meireles","Fortaleza","Ceara","Brasil",listarespostas30,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude33), mPostoSaude33));
			
			List<String> listarespostas31 = new ArrayList<String>();
			listarespostas31.add("4");
			listarespostas31.add("1");
			listarespostas31.add("3");
			Postosaude mPostoSaude34= new Postosaude("31","Rua Ildefonso Albano 464","Meireles","Fortaleza","Ceara","Brasil",listarespostas31,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude34), mPostoSaude34));
			
			List<String> listarespostas32 = new ArrayList<String>();
			listarespostas32.add("4");
			listarespostas32.add("1");
			listarespostas32.add("4");
			Postosaude mPostoSaude35= new Postosaude("32","Av Monsenhor Tabosa 912","Centro","Fortaleza","Ceara","Brasil",listarespostas32,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude35), mPostoSaude35));
			
			List<String> listarespostas33 = new ArrayList<String>();
			listarespostas33.add("4");
			listarespostas33.add("2");
			listarespostas33.add("1");
			Postosaude mPostoSaude36= new Postosaude("33","Rua Antonio Augusto 243","Aldeota","Fortaleza","Ceara","Brasil",listarespostas33,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude36), mPostoSaude36));
	
			List<String> listarespostas34 = new ArrayList<String>();
			listarespostas34.add("4");
			listarespostas34.add("2");
			listarespostas34.add("2");
			Postosaude mPostoSaude37= new Postosaude("34","Rua Joao Cordeiro 209","Praia Iracema","Fortaleza","Ceara","Brasil",listarespostas34,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude37), mPostoSaude37));
			
			List<String> listarespostas35 = new ArrayList<String>();
			listarespostas35.add("4");
			listarespostas35.add("2");
			listarespostas35.add("3");
			Postosaude mPostoSaude38= new Postosaude("35","Rua Xavier de Castro 2","Praia Iracema","Fortaleza","Ceara","Brasil",listarespostas35,0,"hola que tal como te va",project_id);
			PostosSaude.save(mUtility.conectionURL(mUtility.formatarEndereco(mPostoSaude38), mPostoSaude38));
			
			
			
			
			
			
			ModelAndView mModelAndView= new ModelAndView("test");
			return mModelAndView;
	}
	
	
	
	
	@RequestMapping(value = "updateoneposto", method = RequestMethod.GET)
	public @ResponseBody String updateOnePosto(@RequestParam("id") String data)
	{
		String message="success";
		
		System.out.println("I received something="+data);
		String id ="";
		data = data.replace("_"," ");
		int a=0;
	for( int i=0; i<data.length();i++)
	{
		if(!data.substring(i,i +1).equals(";"))
		{
			id += data.substring(i,i +1);
		}
		else
		{
			//SALIR DEL FOR
			a=i+1;
			break;
		}
	
		
	}
	Postosaude postosaude =PostosSaude.findById(Integer.parseInt(id));
	String aux="";
	List<String> listaRespostas = new ArrayList<String>();
	for(int i=a;i<data.length();i++)
	{
		if(!data.substring(i,i +1).equals("="))
		{
			aux += data.substring(i,i +1);
			
		}
		else
		{
			i++;
			if(aux.equals("nome posto saude"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setNomePostoSaude="+aux);
				postosaude.setNome_posto_saude(aux);
				aux="";
			}
			else if(aux.equals("descricao"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setDescricao="+aux);
				postosaude.setDescricao(aux);
				aux="";
			}
			else if(aux.equals("latitude"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setLatitude="+aux);
				postosaude.setLatitude(Double.parseDouble(aux));
				aux="";
			}
			else if(aux.equals("longitude"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setLongitude="+aux);
				postosaude.setLongitude(Double.parseDouble(aux));
				aux="";
			}
			else if(aux.equals("endereco"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setEndereco="+aux);
				postosaude.setEndereco(aux);
				aux="";
			}
			else if(aux.equals("barrio ou nome municipio"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setBarrio_ou_nome_municipio="+aux);
				postosaude.setBarrio_ou_nome_municipio(aux);
				aux="";
			}
			else if(aux.equals("cidade"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setCidade="+aux);
				postosaude.setCidade(aux);
				aux="";
			}
			else if(aux.equals("estado"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setEstado="+aux);
				postosaude.setEstado(aux);
				aux="";
			}
			else if(aux.equals("pais"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("setPais="+aux);
				postosaude.setPais(aux);
				aux="";
			}
		
			else if(aux.equals("lista respostas"))
			{
				aux="";
				while(!data.substring(i,i+1).equals(";"))
				{
					aux+=data.substring(i,i+1);
					i++;
				}
				System.out.println("adding resposta to listaRespostas="+aux);
				listaRespostas.add(aux);
				aux="";
			}
		}
	}
	System.out.println("setLista_respostas size="+listaRespostas.size());
	postosaude.setLista_respostas(listaRespostas);
	
	PostosSaude.save(postosaude);
		return message;
	}
	 

	//DELETE POSTOS DE SAUDE
	@RequestMapping(value = "delete", method = RequestMethod.GET)
	public ModelAndView DeletePostoSaude(Principal p,HttpServletResponse response) 
	{
		
		System.out.println("Dentor de DeletePostosSaude(); Numero Postos="+Lists.newArrayList(PostosSaude.findAll()).size());
		if(p.getName().equals("admin"))
		{
			List<Postosaude> locals = Lists.newArrayList(PostosSaude.findAll());
			for(int i=0;i<locals.size();i++)
			{
				PostosSaude.findById(locals.get(i).getId()).deleteAllCompleted_survey();
			}
			CompletedSurveys.deleteAll();
			PostosSaude.deleteAll();
			Clientes.deleteAll();
			List<Project> projects = Lists.newArrayList(Projects.findAll());
			for(int i=0;i<projects.size();i++)
			{
				Projects.findById(projects.get(i).getId()).removeParametros();
				Projects.findById(projects.get(i).getId()).deleteAllSurvey();
			}
			parametros.deleteAll();
			Surveys.deleteAll();
			Projects.deleteAll();
			CreditoClientesBanco.deleteAll();
			System.out.println(" numero de clientes Banco="+Lists.newArrayList(CreditoClientesBanco.findAll()).size());
			
		}
		else
		{
			try {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}	
		System.out.println("Saliendo!!! de DeletePostosSaude(); Numero Postos="+Lists.newArrayList(PostosSaude.findAll()).size());
		ModelAndView mModelAndView= new ModelAndView("test");
		return mModelAndView;
	}
	//MOSTRAR POSTOS
	@RequestMapping(value = "show", method = RequestMethod.GET)
	public ModelAndView ShowPostoSaude(Principal p,HttpServletResponse response) 
	{
		
		System.out.println("Dentor de ShowPostosSaude(); Numero Postos="+Lists.newArrayList(PostosSaude.findAll()).size());
		for(int i=0;i<Lists.newArrayList(PostosSaude.findAll()).size();i++)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Posto="+Lists.newArrayList(PostosSaude.findAll()).get(i).getId());
			System.out.println("Nome Posto="+Lists.newArrayList(PostosSaude.findAll()).get(i).getNome_posto_saude());
			System.out.println("Latitud="+Lists.newArrayList(PostosSaude.findAll()).get(i).getLatitude());
			System.out.println("Longitud="+Lists.newArrayList(PostosSaude.findAll()).get(i).getLongitude());
			
		}
		System.out.println("Dentor de Clientes(); Numero Clientes="+Lists.newArrayList(Clientes.findAll()).size());
		for(int i=0;i<Lists.newArrayList(Clientes.findAll()).size();i++)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~~~");
			System.out.println("loggingName="+Lists.newArrayList(Clientes.findAll()).get(i).getLogginname());
		}
		System.out.println("Dentor de Preojectos(); Numero Projectos="+Lists.newArrayList(Projects.findAll()).size());
		for(int i=0;i<Lists.newArrayList(Projects.findAll()).size();i++)
		{
			System.out.println("~~~~~~~~~~~~~~~~~~~~");
			System.out.println("Nome do projeto!="+Lists.newArrayList(Projects.findAll()).get(i).getProject_name());
			System.out.println("id_project!!!!!!!!!===="+Lists.newArrayList(Projects.findAll()).get(i).getId());
			System.out.println("numero de preguntas  del questionario="+Lists.newArrayList(Projects.findAll()).get(i).getAllSurveyQuestions().size());
			
			int id_project = (int) Lists.newArrayList(Projects.findAll()).get(i).getId();
			
			for(int j=0;j<Lists.newArrayList(PostosSaude.findAll()).size();j++)
			{
				if( Lists.newArrayList(PostosSaude.findAll()).get(j).getProject_id() == id_project)
				{
					System.out.println("Para el posto con id="+Lists.newArrayList(PostosSaude.findAll()).get(j).getId()+" ,Numero de questionarios Respondidos="+Lists.newArrayList(PostosSaude.findAll()).get(j).getList_completed_survey().size());
				}
			}
		}
		System.out.println("Dentor de RESPOSTAS(); Numero RESPOSTAS="+Lists.newArrayList(parametros.findAll()).size());
	
		ModelAndView mModelAndView= new ModelAndView("test");
		return mModelAndView;
	}
	private List<String> FilterCodification (String respostas)
	{
		List<String> respostasFiltro = new ArrayList<String>();
		String aux="";
		for( int i=0;i<respostas.length();i++)
		{
			if(!respostas.substring(i,i+1).equals("-"))
			{
				aux += respostas.substring(i,i+1);
			}
			else
			{
				respostasFiltro.add(aux);
				aux="";
			}
		}
		for (int i=0;i<respostasFiltro.size();i++)
		{
			System.out.println("Pergunta "+(i+1)+" :"+respostasFiltro.get(i));
		}
		return respostasFiltro;
	}

}



