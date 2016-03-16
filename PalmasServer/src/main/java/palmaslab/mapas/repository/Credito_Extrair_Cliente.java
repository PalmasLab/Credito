package palmaslab.mapas.repository;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;



	public class Credito_Extrair_Cliente {

		ArrayList <Credito_clientes_banco> listaRespostas;
		final static String FILIAL_A_FILTRAR ="001";
		public Credito_Extrair_Cliente()
		{
			listaRespostas = new ArrayList<Credito_clientes_banco>();
		
		}
		
		
		public ArrayList<Credito_clientes_banco> lerArquivo(ArrayList<Credito_clientes_banco> clientes_database)
		{
			try {
	            LocalFileManager lfm = LocalFileManager.get();
	           FileInputStream file = new FileInputStream(new File(lfm.getTargetDir()+"/mapa_credito_excel.xls"));
		    //    FileInputStream file = new FileInputStream(new File("C://Users//Lu//Desktop//TestExcel//test.xls"));
					           
	            //Get the workbook instance for XLS file 
				HSSFWorkbook  workbook = new HSSFWorkbook(file);
	           
	            //Get first sheet from the workbook
	            HSSFSheet sheet = workbook.getSheetAt(0);
	             
	            //Iterate through each rows from first sheet
	            Iterator<Row> rowIterator = sheet.iterator();
	            int contadorRows = 0;
	            boolean cepinned = false;
	            while(rowIterator.hasNext()) {
	             
	            	
	            	
	            		System.out.println("contadorRows="+contadorRows);
		            	Row row = rowIterator.next();
		            	if(contadorRows >2)
		            	{
		                Credito_clientes_banco mClientesBanco = new Credito_clientes_banco();
		               
		                //For each row, iterate through each columns
		                Iterator<Cell> cellIterator = row.cellIterator();
		                
		                while(cellIterator.hasNext()) {
		                     
		                    Cell cell = cellIterator.next();
		                    
		                    if(cell.getColumnIndex() == 0 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addContrato(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 1 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	     
		                    	
		                    	mClientesBanco.setFilial(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 2 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {
		                    	 
		                    	mClientesBanco.setCodigocliente(Long.parseLong(cell.getStringCellValue()));
		                    }
		                    else if(cell.getColumnIndex() == 3 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setNome_cliente(cell.getStringCellValue());
		                    }
		                  
		                    else if(cell.getColumnIndex() == 4 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setNascimento(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 5 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setSexo(cell.getStringCellValue());
		                    }
		                 
		                    	                   
		                    else if(cell.getColumnIndex() == 7 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addAtividade(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 8 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addCodigo(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 9 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addDescricao(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 10 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setEndereco(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 11 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setBairro(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 12 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setCidade(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 13 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setEstado(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 14 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setCEP(cell.getStringCellValue());
		                    	cepinned = true;
		                    }
		                    else if(cell.getColumnIndex() == 14 && cell.getCellType() ==  Cell.CELL_TYPE_NUMERIC && !cepinned)
		                    {	        	         
		                    	mClientesBanco.setCEP(cell.getNumericCellValue()+"");
		                    }
		                    else if(cell.getColumnIndex() == 15 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	
		                    	mClientesBanco.setFone(cell.getStringCellValue());
		                    }
		                    else if(cell.getColumnIndex() == 15 && cell.getCellType() !=  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	
		                    	mClientesBanco.setFone("");
		                    }
		                    
		                    else if(cell.getColumnIndex() == 16 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addConstituicao(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 17 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setRenda_familiar(cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 18 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {
						mClientesBanco.addNumero_pessoas("");	        	         
		                    	mClientesBanco.setNumero_pessoas(0,cell.getStringCellValue());
		                    }
		                 
		                    else if(cell.getColumnIndex() == 19 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.setRenda_per_capita(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 20 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addValor_financiado(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 21 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addNumero_parcelas(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 22 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addRenovacao(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 23 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addLiberacao(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 24 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addSituacao(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 25 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addBF(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 26 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addRisco(cell.getStringCellValue());
		                    }
		                    
		                    else if(cell.getColumnIndex() == 27 && cell.getCellType() ==  Cell.CELL_TYPE_STRING)
		                    {	        	         
		                    	mClientesBanco.addSaldo_devedor(cell.getStringCellValue());
		                    }
		                 
		                }
		                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		                Date date = new Date();
		                mClientesBanco.setData_ultima_atualizacao(dateFormat.format(date)); //2014/08/06 15:59:48
		                boolean jaFoiAdicionado=false;
		                boolean jaFoiAtualizado=false;
		                if(mClientesBanco.getSaldo_devedor().get(0).equals("0,00"))
		                {
		                	List<String> liquidado = new ArrayList<String>();
		                	liquidado.add("LIQUIDADO");
		                	mClientesBanco.setSituacao(liquidado);
		                	System.out.println("Saldo Devedor = 0,00 --> Mudado a LIQUIDADO ");
		                }
		                if(mClientesBanco.getFilial().equals(FILIAL_A_FILTRAR))
		                {
			                if(!clientes_database.isEmpty())
			                {
			            //    	System.out.println("clientes de la base de datos NO nulos!! size = "+clientes_database.size());
			                	for( Credito_clientes_banco cliente_antigo : clientes_database )
			                	{
			                		//COMPARAMOS SI ESE CLIENTE EXISTE
			                	/*	System.out.println("COMPARATIVA CODIGO CLIENTE");
			                		System.out.println("cliente_antigo.getCodigo_cliente().get(0) = "+cliente_antigo.getCodigo_cliente().get(0));
			                		System.out.println("mClientesBanco.getCodigo_cliente().get(0) = "+mClientesBanco.getCodigo_cliente().get(0));*/
			                		if(cliente_antigo.getCodigocliente() == mClientesBanco.getCodigocliente())
			                		{
			                	//		System.out.println("cliente antiguo con le mismo codigo que uno del excel! = "+cliente_antigo.getCodigo_cliente().get(0));
			                			//SI EXISTE, MIRAMOS SI SE ACTUALIZA CONTRATO
			                			for( int i = 0; i < cliente_antigo.getContrato().size();i++)
			                			{
			                		/*		System.out.println("COMPARATIVA CONTRATO");
					                		System.out.println("cliente_antigo.getCodigo_cliente().get(i) = "+cliente_antigo.getContrato().get(i));
					                		System.out.println("mClientesBanco.getContrato().get(0)         = "+mClientesBanco.getContrato().get(0));*/
			                				if(cliente_antigo.getContrato().get(i).equals(mClientesBanco.getContrato().get(0)))
			                				{
			                			//		System.out.println("Cliente ATUALIZADO CodigoCliente = "+cliente_antigo.getCodigo_cliente()+ " com Contrato = "+mClientesBanco.getContrato());
													jaFoiAtualizado = true;
													System.out.println("[cliente_antigo.getContrato().get(i).equals(mClientesBanco.getContrato().get(0))] i = "+i);
												//getFilial
												
												//getAtividade
						    		                		if(mClientesBanco.getAtividade().isEmpty())
						    		                		{
														cliente_antigo.getAtividade().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getAtividade().isEmpty())
													{
														cliente_antigo.getAtividade().add(mClientesBanco.getAtividade().get(0));
													}
													else
													{
														cliente_antigo.getAtividade().set(i,mClientesBanco.getAtividade().get(0));
													}
						    		                		}	
												//getCodigo
						    		                		if(mClientesBanco.getCodigo().isEmpty())
						    		                		{
														cliente_antigo.getCodigo().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getCodigo().isEmpty())
													{
														cliente_antigo.getCodigo().add(mClientesBanco.getCodigo().get(0));
													}
													else
													{
														cliente_antigo.getCodigo().set(i,mClientesBanco.getCodigo().get(0));
													}
						    		                		}	
												//getDescricao
						    		                		if(mClientesBanco.getDescricao().isEmpty())
						    		                		{
														cliente_antigo.getDescricao().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getDescricao().isEmpty())
													{
														cliente_antigo.getDescricao().add(mClientesBanco.getDescricao().get(0));
													}
													else
													{
														cliente_antigo.getDescricao().set(i,mClientesBanco.getDescricao().get(0));
													}
						    		                		}	
												
												//getFone
						    		                if(mClientesBanco.getFone()!=null)	
						    		                {
						    		                	if(cliente_antigo.getFone() != null)
						    		                	{
															if(cliente_antigo.getFone().equals("")  )
															{
																cliente_antigo.setFone(mClientesBanco.getFone());
															}
						    		                	}	
						    		                }
												//getConstituicao
						    		                		if(mClientesBanco.getConstituicao().isEmpty())
						    		                		{
														cliente_antigo.getConstituicao().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getConstituicao().isEmpty())
													{
														cliente_antigo.getConstituicao().add(mClientesBanco.getConstituicao().get(0));
													}
													else
													{
														cliente_antigo.getConstituicao().set(i,mClientesBanco.getConstituicao().get(0));
													}
						    		                		}	
												//getRenda_familiar
						    		           
												//getNumero_pessoas
						    		                		if(mClientesBanco.getNumero_pessoas().isEmpty())
						    		                		{
														cliente_antigo.getNumero_pessoas().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getNumero_pessoas().isEmpty())
													{
														cliente_antigo.getNumero_pessoas().add(mClientesBanco.getNumero_pessoas().get(0));
													}
													else
													{
														System.out.println(" [getNumeroPessoas->else->else] i = "+i);
														System.out.println("[getNumeroPessoas->else->else] CodigoCliente = "+ cliente_antigo.getCodigocliente());
														if(cliente_antigo.getNumero_pessoas().size()>i)
														{
															cliente_antigo.getNumero_pessoas().set(i,mClientesBanco.getNumero_pessoas().get(0));															
														}
														else
														{
															cliente_antigo.getNumero_pessoas().add(mClientesBanco.getNumero_pessoas().get(0));
														}

													}
						    		                		}	
												//getRenda_per_capita
						    		             
												//getValor_financiado
						    		                		if(mClientesBanco.getValor_financiado().isEmpty())
						    		                		{
														cliente_antigo.getValor_financiado().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getValor_financiado().isEmpty())
													{
														cliente_antigo.getValor_financiado().add(mClientesBanco.getValor_financiado().get(0));
													}
													else
													{
														cliente_antigo.getValor_financiado().set(i,mClientesBanco.getValor_financiado().get(0));
													}
						    		                		}	
												//getNumero_parcelas
						    		                		if(mClientesBanco.getNumero_parcelas().isEmpty())
						    		                		{
														cliente_antigo.getNumero_parcelas().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getNumero_parcelas().isEmpty())
													{
														cliente_antigo.getNumero_parcelas().add(mClientesBanco.getNumero_parcelas().get(0));
													}
													else
													{
														cliente_antigo.getNumero_parcelas().set(i,mClientesBanco.getNumero_parcelas().get(0));
													}
						    		                		}	
												//getRenovacao
						    		                		if(mClientesBanco.getRenovacao().isEmpty())
						    		                		{
														cliente_antigo.getRenovacao().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getRenovacao().isEmpty())
													{
														cliente_antigo.getRenovacao().add(mClientesBanco.getRenovacao().get(0));
													}
													else
													{
														cliente_antigo.getRenovacao().set(i,mClientesBanco.getRenovacao().get(0));
													}
						    		                		}	
												//getLiberacao
						    		                		if(mClientesBanco.getLiberacao().isEmpty())
						    		                		{
														cliente_antigo.getLiberacao().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getLiberacao().isEmpty())
													{
														cliente_antigo.getLiberacao().add(mClientesBanco.getLiberacao().get(0));
													}
													else
													{
														cliente_antigo.getLiberacao().set(i,mClientesBanco.getLiberacao().get(0));
													}
						    		                		}	
												//getSituacao
						    		                		if(mClientesBanco.getSituacao().isEmpty())
						    		                		{
														cliente_antigo.getSituacao().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getSituacao().isEmpty())
													{
														cliente_antigo.getSituacao().add(mClientesBanco.getSituacao().get(0));
													}
													else
													{
														cliente_antigo.getSituacao().set(i,mClientesBanco.getSituacao().get(0));
													}
						    		                		}	
												//getBF
						    		                		if(mClientesBanco.getBF().isEmpty())
						    		                		{
														cliente_antigo.getBF().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getBF().isEmpty())
													{
														cliente_antigo.getBF().add(mClientesBanco.getBF().get(0));
													}
													
						    		                		}	
												//getRisco
						    		                		if(mClientesBanco.getRisco().isEmpty())
						    		                		{
														cliente_antigo.getRisco().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getRisco().isEmpty())
													{
														cliente_antigo.getRisco().add(mClientesBanco.getRisco().get(0));
													}
													else
													{
														cliente_antigo.getRisco().set(i,mClientesBanco.getRisco().get(0));
													}
						    		                		}	
												//getSaldo_devedor
						    		                		if(mClientesBanco.getSaldo_devedor().isEmpty())
						    		                		{
														cliente_antigo.getSaldo_devedor().add("");
						    		                		}
						    		                		else
						    		                		{
													if(cliente_antigo.getSaldo_devedor().isEmpty())
													{
														cliente_antigo.getSaldo_devedor().add(mClientesBanco.getSaldo_devedor().get(0));
													}
													else
													{
														cliente_antigo.getSaldo_devedor().set(i,mClientesBanco.getSaldo_devedor().get(0));
													}
						    		                		}	


			                				}//cierra el if
			                			}//cierra el FOR comprobador de Contratos
			                			//SI EXISTE Y EL CONTRATO NO FUE ACTUALIZADO, HAY QUE AnhADIRLO!
			                			if(!jaFoiAtualizado)
			                			{
			                				cliente_antigo.addContrato(mClientesBanco.getContrato().get(0));
					                		if(mClientesBanco.getFilial() != null && cliente_antigo.getFilial()!=null)
					                		{
					                			if(!mClientesBanco.getFilial().equals("") && !cliente_antigo.getFilial().equals(""))
					                			cliente_antigo.setFilial(mClientesBanco.getFilial());
					                		}
					                		
					                		
					                		
					                		
					                		if(mClientesBanco.getAtividade().isEmpty())
					                		{
					                			cliente_antigo.addAtividade("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addAtividade(mClientesBanco.getAtividade().get(0));
					                		}
					                		if(mClientesBanco.getCodigo().isEmpty())
					                		{
					                			cliente_antigo.addCodigo("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addCodigo(mClientesBanco.getCodigo().get(0));
					                		}
					                		if(mClientesBanco.getDescricao().isEmpty())
					                		{
					                			cliente_antigo.addDescricao("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addDescricao(mClientesBanco.getDescricao().get(0));
					                		}
					                		//endereco
					                		if(mClientesBanco.getEndereco() != null && cliente_antigo.getEndereco()!=null)
					                		{
					                			if(!mClientesBanco.getEndereco().equals("") && !cliente_antigo.getEndereco().equals(""))
					                			cliente_antigo.setEndereco(mClientesBanco.getEndereco());
					                		}
					                		
					                		
					                		//bairro
					                		if(mClientesBanco.getBairro() != null && cliente_antigo.getBairro()!=null)
					                		{
					                			if(!mClientesBanco.getBairro().equals("") && !cliente_antigo.getBairro().equals(""))
					                			cliente_antigo.setBairro(mClientesBanco.getBairro());
					                		}
					                		//cidade
					                		if(mClientesBanco.getCidade() != null && cliente_antigo.getCidade()!=null)
					                		{
					                			if(!mClientesBanco.getCidade().equals("") && !cliente_antigo.getCidade().equals(""))
					                			cliente_antigo.setCidade(mClientesBanco.getCidade());
					                		}
					                		//estado
					                		if(mClientesBanco.getEstado() != null && cliente_antigo.getEstado()!=null)
					                		{
					                			if(!mClientesBanco.getEstado().equals("") && !cliente_antigo.getEstado().equals(""))
					                			cliente_antigo.setEstado(mClientesBanco.getEstado());
					                		}
					                		//cep
					                		if(mClientesBanco.getCEP() != null && cliente_antigo.getCEP()!=null)
					                		{
					                			if(!mClientesBanco.getCEP().equals("") && !cliente_antigo.getCEP().equals(""))
					                			cliente_antigo.setCEP(mClientesBanco.getCEP());
					                		}
					                		//fone
					                		if(mClientesBanco.getFone() != null && cliente_antigo.getFone()!=null)
					                		{
					                			if(!mClientesBanco.getFone().equals("") && !cliente_antigo.getFone().equals(""))
					                			cliente_antigo.setFone(mClientesBanco.getFone());
					                		}
					                		
					                		if(mClientesBanco.getConstituicao().isEmpty())
					                		{
					                			cliente_antigo.addConstituicao("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addConstituicao(mClientesBanco.getConstituicao().get(0));
					                		}
					                		//renda familiar
					                		if(mClientesBanco.getRenda_familiar() != null && cliente_antigo.getRenda_familiar()!=null)
					                		{
					                			if(!mClientesBanco.getRenda_familiar().equals("") && !cliente_antigo.getRenda_familiar().equals(""))
					                			cliente_antigo.setRenda_familiar(mClientesBanco.getRenda_familiar());
					                		}
					                	
					                		if(mClientesBanco.getNumero_pessoas().isEmpty())
					                		{
					                			cliente_antigo.addNumero_pessoas("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addNumero_pessoas(mClientesBanco.getNumero_pessoas().get(0));
					                		}
					                		//renda per capita
					                		if(mClientesBanco.getRenda_per_capita() != null && cliente_antigo.getRenda_per_capita()!=null)
					                		{
					                			if(!mClientesBanco.getRenda_per_capita().equals("") && !cliente_antigo.getRenda_per_capita().equals(""))
					                			cliente_antigo.setRenda_per_capita(mClientesBanco.getRenda_per_capita());
					                		}
					                		
					                		if(mClientesBanco.getValor_financiado().isEmpty())
					                		{
					                			cliente_antigo.addValor_financiado("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addValor_financiado(mClientesBanco.getValor_financiado().get(0));
					                		}
					                		if(mClientesBanco.getNumero_parcelas().isEmpty())
					                		{
					                			cliente_antigo.addNumero_parcelas("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addNumero_parcelas(mClientesBanco.getNumero_parcelas().get(0));
					                		}
					                		if(mClientesBanco.getRenovacao().isEmpty())
					                		{
					                			cliente_antigo.addRenovacao("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addRenovacao(mClientesBanco.getRenovacao().get(0));
					                		}
					                		if(mClientesBanco.getLiberacao().isEmpty())
					                		{
					                			cliente_antigo.addLiberacao("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addLiberacao(mClientesBanco.getLiberacao().get(0));
					                		}
					                		if(mClientesBanco.getSituacao().isEmpty())
					                		{
					                			cliente_antigo.addSituacao("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addSituacao(mClientesBanco.getSituacao().get(0));
					                		}
					                		if(mClientesBanco.getBF().isEmpty())
					                		{
					                			cliente_antigo.addBF("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addBF(mClientesBanco.getBF().get(0));
					                		}
					                		if(mClientesBanco.getRisco().isEmpty())
					                		{
					                			cliente_antigo.addRisco("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addRisco(mClientesBanco.getRisco().get(0));
					                		}
					                		if(mClientesBanco.getSaldo_devedor().isEmpty())
					                		{
					                			cliente_antigo.addSaldo_devedor("");
					                		}
					                		else
					                		{
					                			cliente_antigo.addSaldo_devedor(mClientesBanco.getSaldo_devedor().get(0));
					                		}
					                		jaFoiAdicionado = true;
			                			}//cierra el if para Adicionar
			                		}//Cierra el IF -> CodigoCliente IGUAL
			                	}//CIERRA EL FOR DE COMPROBACION DE TODOS LOS CLIENTES ANTIGOS
			                }//CIERRA EL IF, los clientes antiguos isNotEmpty
			               
			                if(clientes_database.size() == 0 || (!jaFoiAdicionado && !jaFoiAtualizado))
			                {
			                	System.out.println("listaRespostas.size() por ahora=="+clientes_database.size());
			                	//clientes_database.add(new PostoSaudeUtility().conectionURLExcel(new PostoSaudeUtility().formatarEnderecoExcel(mClientesBanco), mClientesBanco));
			                	clientes_database.add( mClientesBanco);
			                }
		            }//CIERRA EL IF DE SI FILIAL ES LA 001
		                
		            }
	            	
	            contadorRows++;
			}
			                    
            //Tamanho de listaRespostas
        //    System.out.println(listaRespostas.size());
            file.close();
		        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
			System.out.println("NUMERO DE CLIENTES FINAL  !!!!! = "+clientes_database.size());
			
			for(int i = 0; i<clientes_database.size(); i++)
			{
				if(!clientes_database.get(i).getGpspositionupdated())
				{
					clientes_database.set(i, new PostoSaudeUtility().conectionURLExcel(new PostoSaudeUtility().formatarEnderecoExcel(clientes_database.get(i)), clientes_database.get(i)));
				}
			}
		return clientes_database;
	}
}
	
	           
		
