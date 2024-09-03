package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// TODO Auto-generated method stub
		// API Key
		String apiKey = "186b38092c984ea2e53be4246937e211";
		// Get the city from the form input
		String city = request.getParameter("city");
		String encodedCity = URLEncoder.encode(city, StandardCharsets.UTF_8.toString());

		// Create the URL for the OpenWeatherMap API request
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey;
		
		// Api integration
		URL url = new URL(apiUrl);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		// Reading the data from the network
		InputStream inputStream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		// System.out.println(reader);

		// Want to store in string
		StringBuilder responseContent = new StringBuilder();

		// Input lene keliye from the reader, will create scanner object
		Scanner scanner = new Scanner(reader);

		while (scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}

		scanner.close();
		//System.out.println(responseContent);
		
		 // Parse the JSON response to extract temperature, date, and humidity
       Gson gson = new Gson();       
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
      //  System.out.println(jsonObject);
        
        //Date & Time
        long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
        String date = new Date(dateTimestamp).toString();   
        //Temperature
        double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
        int temperatureCelsius = (int) (temperatureKelvin - 273.15);
       
        //Humidity
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        
        //Wind Speed
        double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        
        //Weather Condition
        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
        
        request.setAttribute("date", date);
        request.setAttribute("city", city);
        request.setAttribute("temperature", temperatureCelsius);
        request.setAttribute("weatherCondition", weatherCondition); 
        request.setAttribute("humidity", humidity);    
        request.setAttribute("windSpeed", windSpeed);
        request.setAttribute("weatherData", responseContent.toString());
		 
        connection.disconnect();
        
        // Forward the request to the weather.jsp page for rendering
        request.getRequestDispatcher("index.jsp").forward(request, response);
	}

}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		URI apiUri = URI.create("https://api.openweathermap.org/data/2.5/weather?q=" + encodedCity + "&appid=" + apiKey);
//		// Create HttpClient and HttpRequest
//				HttpClient client = HttpClient.newHttpClient();
//				HttpRequest httpRequest = HttpRequest.newBuilder(apiUri).GET().build();
//
//				try {
//					// Send the request and get the response
//					HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
//
//					// Check if the response code is 200 (OK)
//					if (httpResponse.statusCode() != 200) {
//						throw new IOException("Received non-OK response code: " + httpResponse.statusCode());
//					}
//
//					// Parse the JSON response to extract temperature, date, and humidity
//					Gson gson = new Gson();
//					JsonObject jsonObject = gson.fromJson(httpResponse.body(), JsonObject.class);
//
//					// Print the JSON object
//					System.out.println(jsonObject);
//
//				} catch (IOException | InterruptedException e) {
//					response.getWriter().println("Error: " + e.getMessage());
//					e.printStackTrace();
//				}
//			}
//		}
//     
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//		
//     
//
