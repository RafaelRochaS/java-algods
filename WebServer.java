import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Class that implements a simple webserver that can obtain files and return error messages.
 */
public class WebServer {
    
    private static final int LISTENING_PORT = 50505;
    private static final String ROOT_DIR = ".";
	
	/**
	 * Main program opens a server socket and listens for connection
	 * requests.  It calls the handleConnection() method to respond
	 * to connection requests.  The program runs in an infinite loop,
	 * unless an error occurs.
	 * @param args ignored
	 */
	public static void main(String[] args) {
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(LISTENING_PORT);
		}
		catch (Exception e) {
			System.out.println("Failed to create listening socket.");
			return;
		}
		System.out.println("Listening on port " + LISTENING_PORT);
		try {
			while (true) {
				Socket connection = serverSocket.accept();
				System.out.println("\nConnection from " 
						+ connection.getRemoteSocketAddress());
                ConnectionThread thread = new ConnectionThread(connection);
                thread.start();
			}
		}
		catch (Exception e) {
			System.out.println("Server socket shut down unexpectedly!");
			System.out.println("Error: " + e);
			System.out.println("Exiting.");
		}
	}

	/**
	 * Handle commuincation with one client connection.  This method reads
	 * lines of text from the client and prints them to standard output.
	 * It continues to read until the client closes the connection or
	 * until an error occurs or until a blank line is read.  In a connection
	 * from a Web browser, the first blank line marks the end of the request.
	 * This method can run indefinitely,  waiting for the client to send a
	 * blank line.
	 * NOTE:  This method does not throw any exceptions.  Exceptions are
	 * caught and handled in the method, so that they will not shut down
	 * the server.
	 * @param connection the connected socket that will be used to
	 *    communicate with the client.
	 */
	private static void handleConnection(Socket connection) {

		try {
			Scanner in = new Scanner(connection.getInputStream());
			while (true) {
				if (!in.hasNextLine())
					break;
				String line = in.nextLine();
				if (line.trim().length() == 0)
					break;
                System.out.println("   " + line);
                String[] request = line.split(" ");
                if (request[0].equals("GET")) {
                    File file = new File(ROOT_DIR + request[1]);
                    if (!file.exists() || file.isDirectory()) {
                        sendErrorResponse(404, connection.getOutputStream());
                        break;
                    } else if (!file.canRead()) {
                        sendErrorResponse(403, connection.getOutputStream());
                    } else {
                        PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
                        out.print("HTTP/1.1 200 OK\r\n");
                        out.print("Connection: close\r\n");
                        out.print("Content-Length: " + file.length() + "\r\n");
                        out.print("Content-Type: " + getMimeType(file.getName()) + "\r\n");
                        out.flush();
                        sendFile(file, connection.getOutputStream());
                        break;
                    }
                } else {
                    sendErrorResponse(501, connection.getOutputStream());
                }
			}
		}
		catch (Exception e) {
            System.out.println("Error while communicating with client: " + e);
		}
		finally {  // make SURE connection is closed before returning!
			try {
				connection.close();
			}
			catch (Exception e) {
			}
			System.out.println("Connection closed.");
		}
    }
    
    private static String getMimeType(String fileName) {

        int pos = fileName.lastIndexOf('.');
        if (pos < 0) // no file extension in name
            return "x-application/x-unknown";
        String ext = fileName.substring(pos + 1).toLowerCase();
        if (ext.equals("txt"))
            return "text/plain";
        else if (ext.equals("html"))
            return "text/html";
        else if (ext.equals("htm"))
            return "text/html";
        else if (ext.equals("css"))
            return "text/css";
        else if (ext.equals("js"))
            return "text/javascript";
        else if (ext.equals("java"))
            return "text/x-java";
        else if (ext.equals("jpeg"))
            return "image/jpeg";
        else if (ext.equals("jpg"))
            return "image/jpeg";
        else if (ext.equals("png"))
            return "image/png";
        else if (ext.equals("gif"))
            return "image/gif";
        else if (ext.equals("ico"))
            return "image/x-icon";
        else if (ext.equals("class"))
            return "application/java-vm";
        else if (ext.equals("jar"))
            return "application/java-archive";
        else if (ext.equals("zip"))
            return "application/zip";
        else if (ext.equals("xml"))
            return "application/xml";
        else if (ext.equals("xhtml"))
            return "application/xhtml+xml";
        else
            return "x-application/x-unknown";
        // Note: x-application/x-unknown is something made up;
        // it will probably make the browser offer to save the file.
    }

    private static void sendFile(File file, OutputStream socketOut) throws IOException {

        InputStream in = new BufferedInputStream(new FileInputStream(file));
        OutputStream out = new BufferedOutputStream(socketOut);
        
        while (true) {
            int x = in.read(); // read one byte from file
            if (x < 0)
                break; // end of file reached
            out.write(x); // write the byte to the socket
        }

        out.flush();
        in.close();
    }

    private static void sendErrorResponse(int errorCode, OutputStream socketOut) {

        try (PrintWriter out = new PrintWriter(socketOut, true)) {
            switch (errorCode) {
                case 404:
                    out.write("HTTP/1.1 404 Not Found\r\n");
                    out.write("Connection: close\r\n");
                    out.write("Content-Type: text/html\r\n");
                    out.write("<html><head><title>Error</title></head><body><h2>Error: 404 Not Found</h2><p>The resource that you requested does not exist on this server.</p></body></html>");
                    out.flush();
                    break;
                case 403:
                    out.write("HTTP/1.1 403 Forbidden\r\n");
                    out.write("Connection: close\r\n");
                    out.write("Content-Type: text/html\r\n");
                    out.write("<html><head><title>Error</title></head><body><h2>Error: 403 Forbidden</h2><p>The resource that you requested cannot be read by you.</p></body></html>");
                    out.flush();
                    break;
                case 501:
                    out.write("HTTP/1.1 501 Not Implemented\r\n");
                    out.write("Connection: close\r\n");
                    out.write("Content-Type: text/html\r\n");
                    out.write("<html><head><title>Error</title></head><body><h2>Error: 501 Not Implemented</h2><p>The requested method is not implemented.</p></body></html>");
                    out.flush();
                    break;
                default:
                    throw new IOException("Error code failed");
            }
        } catch (Exception e) {
            System.out.println("Something went wrong while trying to send error response: " + e.getMessage());
        }
    }

    private static class ConnectionThread extends Thread {

        Socket connection;

        ConnectionThread(Socket connection) {
            this.connection = connection;
        }

        public void run() {
            handleConnection(connection);
        }
    }
}
