package palmaslab.mapas.repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.HttpURLConnection;  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostoSaudeUtility {

	static private String unacomilla="\"";
	static public String Pergunta1 = "Cobertura das UPAS (Pessoas por equipe)";
	static public String Pergunta2 = "Infaestrutura";
	static public String Pergunta3 = "Equipes Medicas";
	
	static public List<String> ListaPerguntas= new ArrayList<String>(Arrays.asList(Pergunta1,Pergunta2,Pergunta3));
	
	static public ArrayList<String> ListaRespostas1 = new ArrayList<String>(Arrays.asList("Todos","0 a 4000", "4001 a 6000", "6001 a 8000","+ de 8001"));
	static public ArrayList<String> ListaRespostas2 = new ArrayList<String>(Arrays.asList("Todos","Reformado", "Nao Reformado"));
	static public ArrayList<String> ListaRespostas3 = new ArrayList<String>(Arrays.asList("Todos","Acima de 75%", "51% a 75%", "26% a 50%","Ate 25%"));
	
	static public List<String> ListaRespostas= new ArrayList<String>(Arrays.asList("{Todos,0 a 4000,4001 a 6000,6001 a 8000,+ de 8001}","{Todos,Reformado,Nao Reformado}","Todos,Acima de 75%,51% a 75%,26% a 50%,Ate 25%}"));
	
    public String formatarEndereco( Postosaude mPostoSaude)
    {
        String enderecoCerto="";
        if(mPostoSaude.getCidade().equals(""))
        {
            enderecoCerto=mPostoSaude.getEndereco().replace(" ", "+")+",+"+mPostoSaude.getBarrio_ou_nome_municipio().replaceAll(" ", "+")+"+"+mPostoSaude.getEstado().replaceAll(" ", "+")+",+"+mPostoSaude.getPais();
        }
        else
        {
            enderecoCerto=mPostoSaude.getEndereco().replace(" ", "+")+",+"+mPostoSaude.getBarrio_ou_nome_municipio().replaceAll(" ", "+")+"+"+mPostoSaude.getCidade().replaceAll(" ", "+")+"+"+mPostoSaude.getEstado().replaceAll(" ", "+")+",+"+mPostoSaude.getPais();
        }
        System.out.println("0- ~~~~~Endereco formatado="+enderecoCerto);
        return enderecoCerto;
    }
    
    public  Postosaude conectionURL(String endereco,Postosaude mPostoSaude)  
    {
       System.out.println("1- Estabelecendo conexao com googlemaps");
        try{
        	
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+endereco+"&sensor=false");
        //URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyArJcwrI-U7j93S8PcQQCdjPXgnqWWWIuU&address="+endereco+"&sensor=false");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        System.out.println("2- HttpURLConnection feita");
     BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
     System.out.println("3-  BufferedReader feita");
     String linea;
     boolean StartCopyingLatitud=false;
     boolean StartCopyingLongitud=false;
     String latitud="0";
     String longitud="0";
     String formated_address="";
      int i=0;
     while ((linea = in.readLine()) != null) {
         
        System.out.println(linea);
        if(StartCopyingLatitud)
        {
            i=0;
            latitud="";
            while(!linea.substring(23+i,23+i+1).equals(","))
            {
            latitud=latitud+linea.substring(23+i,23+i+1);
            i++;
            }
            System.out.println("LATITUDEEEEEEE!!!!!!!!!!!!!:"+latitud);
            StartCopyingLatitud=false;
            StartCopyingLongitud=true;
        }
        else if(StartCopyingLongitud)
        {
           
            
            longitud=linea.substring(23,linea.length());
           
            
            StartCopyingLatitud=false;
            StartCopyingLongitud=false;
            System.out.println("LONGITUDE!!!!!!!!!!!!!!!"+longitud);
        }
        if(linea.startsWith("            "+unacomilla+"location"+unacomilla+" : {"))
        {
            StartCopyingLatitud=true;
            System.out.println("encontrada posicion gps!");
        }
        else if(linea.startsWith("         \"types\" : [ \"street_address\""))
     {
        	mPostoSaude.setLatitude(Double.parseDouble(latitud));
        	mPostoSaude.setLongitude(Double.parseDouble(longitud));
         if(!formated_address.equals(""))
         {
        	 mPostoSaude.setEndereco(formated_address);
         }
     }
        //         "types" : [ "route" ]
           else if(linea.startsWith("         \"types\" : [ \"route\" ]"))
     {
        	   mPostoSaude.setLatitude(Double.parseDouble(latitud));
        	   mPostoSaude.setLongitude(Double.parseDouble(longitud));
          if(!formated_address.equals(""))
         {
        	  mPostoSaude.setEndereco(formated_address);
         }
     }
     else if(linea.startsWith("         \"formatted_address\" : "))
     {
         formated_address=linea.substring(32, linea.length()-2);
         mPostoSaude.setEndereco(formated_address);
           System.out.println("Formatted Address:"+formated_address);
     }
     
        
   }
    
       }catch(MalformedURLException e){System.out.println(e.getMessage());}catch(IOException o){System.out.println(o.getMessage());}
     return mPostoSaude;
    
    }
    
    
    public String formatarEnderecoExcel( Credito_clientes_banco ccb)
    {
        String enderecoCerto="";
        if(ccb.getCidade() == null)
        {
            enderecoCerto=ccb.getEndereco().replace(" ", "+")+",+"+ccb.getBairro().replaceAll(" ", "+")+"+"+ccb.getEstado().replaceAll(" ", "+")+",+"+"Brasil";
        }
        else if(ccb.getBairro() == null)
        {
        	enderecoCerto=ccb.getEndereco().replace(" ", "+")+",+"+ccb.getCidade().replaceAll(" ", "+")+"+"+ccb.getEstado().replaceAll(" ", "+")+",+"+"Brasil";
        }
        else if(ccb.getEndereco() == null)
        {
        	enderecoCerto=ccb.getBairro().replaceAll(" ", "+")+"+"+ccb.getCidade().replaceAll(" ", "+")+"+"+ccb.getEstado().replaceAll(" ", "+")+",+"+"Brasil";
        }
        else
        {
            enderecoCerto=ccb.getEndereco().replace(" ", "+")+",+"+ccb.getBairro().replaceAll(" ", "+")+"+"+ccb.getCidade().replaceAll(" ", "+")+"+"+ccb.getEstado().replaceAll(" ", "+")+",+"+"Brasil";
        }
       
        return enderecoCerto;
    }
    
    public  Credito_clientes_banco conectionURLExcel(String endereco,Credito_clientes_banco ccb)  
    {
       
        try{
        	
        URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+endereco+"&sensor=false");
        //URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?key=AIzaSyArJcwrI-U7j93S8PcQQCdjPXgnqWWWIuU&address="+endereco+"&sensor=false");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
   
     BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
   
     String linea;
     boolean StartCopyingLatitud=false;
     boolean StartCopyingLongitud=false;
     String latitud="0";
     String longitud="0";
     String formated_address="";
      int i=0;

	System.out.println("endereço inicial! ------------------>> "+endereco);
System.out.println("endereço cliente! -------------------->>"+ccb.getEndereco());
     while ((linea = in.readLine()) != null) {
         
       System.out.println(linea);
        if(StartCopyingLatitud)
        {
            i=0;
            latitud="";
            while(!linea.substring(23+i,23+i+1).equals(","))
            {
            latitud=latitud+linea.substring(23+i,23+i+1);
            i++;
            }
            
            StartCopyingLatitud=false;
            StartCopyingLongitud=true;
        }
        else if(StartCopyingLongitud)
        {
           
            
            longitud=linea.substring(23,linea.length());
           
            
            StartCopyingLatitud=false;
            StartCopyingLongitud=false;
          
        }
        if(linea.startsWith("            "+unacomilla+"location"+unacomilla+" : {"))
        {
            StartCopyingLatitud=true;
          
        }
        else if(linea.startsWith("         \"types\" : [ \"street_address\""))
     {
			System.out.println("GPS Position added.");
        	ccb.setLatitude(Double.parseDouble(latitud));
        	ccb.setLongitude(Double.parseDouble(longitud));
        	ccb.setGpspositionupdated(true);
         if(!formated_address.equals(""))
         {
		
        	 	ccb.setEndereco(formated_address);
		
         }
     }
        //         "types" : [ "route" ]
           else if(linea.startsWith("         \"types\" : [ \"route\" ]"))
     {
			System.out.println("GPS Position added.");
        	   ccb.setLatitude(Double.parseDouble(latitud));
        	   ccb.setLongitude(Double.parseDouble(longitud));
        	   ccb.setGpspositionupdated(true);
          if(!formated_address.equals(""))
         {
        
        	 	ccb.setEndereco(formated_address);
		
         }
     }
     else if(linea.startsWith("         \"formatted_address\" : "))
     {
         formated_address=linea.substring(32, linea.length()-2);
         ccb.setEndereco(formated_address);
           
     }
     
        
   }
    
       }catch(MalformedURLException e){System.out.println(e.getMessage());}catch(IOException o){System.out.println(o.getMessage());}
     return ccb;
    
    }
    
    
    
    
}
