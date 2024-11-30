
# Simple Java RMI Implementation

This repository contains an implementation of a simple Remote Method Invocation (RMI) system in Java. The system allows the invocation of remote methods in such a way that, to the programmer, remote method calls appear to be local method calls.

## Features

- **Custom RMI Implementation**: The system does not use the built-in Java RMI package. Instead, it implements its own RMI framework.
- **Multiple Clients**: Supports multiple clients connecting to a remote object simultaneously.
- **Inheritance-based Design**: Inheritance is used to enforce method implementation and maintain consistency on both client and server sides.
- **Dynamic Binding**: Client-server binding is dynamic, based on class names and the implementation version of the classes.
- **Argument and Return Type Support**: The system supports primitive types (`int`, `boolean`, `char`, and `double`) for both arguments and return types, as well as object types.
- **Good Programming Practices**: The implementation follows good programming practices in design and structure.

## How It Works

- **Client-Server Model**: The system is based on a typical client-server model where the client calls remote methods on the server. The system hides the complexity of the remote calls, making them appear local.
- **Dynamic Binding**: The client dynamically binds to the server using the class name and versioning information. This ensures flexibility in version management.
- **Inheritance for Consistency**: Both client and server must adhere to a common interface, ensuring method signatures and behavior consistency.

## Supported Data Types

- **Primitive Types**: `int`, `boolean`, `char`, and `double`
- **Objects**: The system supports passing objects as arguments and return types.

## Setup

### Prerequisites

- Java Development Kit (JDK) 8 or higher.

### Running the Server

1. Compile the server-side code:
   ```bash
   javac Server.java
   ```

2. Run the server:
   ```bash
   java Server
   ```

### Running the Client

1. Compile the client-side code:
   ```bash
   javac Client.java
   ```

2. Run the client:
   ```bash
   java Client
   ```

## Usage

1. The client will connect to the server and invoke remote methods just like local method calls.
2. The server will execute the requested remote method and return the result to the client.
3. **Example Method: `getPrice`**

   The `getPrice(String symbol)` method demonstrates how to retrieve the price of a stock symbol by invoking a remote service. It performs the following steps:

   - Reads the server’s IP and port from a file.
   - Uses dynamic lookup to find the remote service address.
   - Constructs a JSON request with the stock symbol.
   - Sends the request and waits for the response, returning the stock price if successful.

   The full implementation of `getPrice` can be found in the [test.java](test.java) file.

4. **Example Usage in Client:**

   In the client application, you would call the remote method like this:

   ```java
   int price = client.getPrice("AAPL");
   System.out.println("Stock Price: " + price);

## Example

Here’s an example of how a remote method might look:

### Remote Interface
```java
public interface RemoteService {
    int add(int a, int b);
    boolean isValid(String input);
}
```

### Server Implementation
```java
public class RemoteServiceImpl extends RemoteService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public boolean isValid(String input) {
        return input != null && !input.isEmpty();
    }
}
```

### Client Implementation
```java
public class Client {
    public static void main(String[] args) {
        try {
            RemoteService service = (RemoteService) RMIClient.connectToServer("RemoteServiceImpl");
            int result = service.add(5, 3);
            System.out.println("Result: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

## Contributing

Feel free to submit issues, suggestions, or pull requests. Contributions are welcome!

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
