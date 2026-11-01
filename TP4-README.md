# TP4-DAR2026 — UDP Chat Service (DatagramSocket)

**Course:** Développement d'applications réparties (DAR) — Faculté des Sciences de Sfax, LSI 3
**Activity:** TP 4 — Sockets en mode non-connecté UDP : Service de chat C/S avec UDP
**Student:** Med Hassan Ben Adi
**Academic year:** 2026 – 2027

---

## 1. Objective

Build a client/server chat application using **UDP** (connectionless mode) with `DatagramSocket` and `DatagramPacket`:

- **Step 2:** one-way chat, where one client sends messages and the server displays them.
- **Step 3:** two-way chat with broadcast, where the server relays each message to all the other clients and the clients send and receive in real time.

## 2. Project structure

```
TP4-DAR2026/
├── README.md
├── step2-unidirectional/
│   ├── Server.java
│   └── Client.java
└── step3-broadcast/
    ├── Server.java
    └── Client.java
```

## 3. How it works

### Step 2 — One-way chat

- **Server:** creates a `DatagramSocket`, binds it to port **1234** with `InetSocketAddress`, and loops on `receive()`. For each datagram it prints the sender's address (IP and port) and the message.
- **Client:** asks for a username, then sends each typed line to the server as a datagram, prefixed with the username (`[Username] : message`).

### Step 3 — Broadcast chat

- **Server:** keeps a `Set<SocketAddress>` of known clients. When a datagram arrives, it adds the sender to the set if it is not already there (this simulates a "connection"), then forwards the message to every registered client **except the sender**.
- **Client:** uses two concurrent threads:
  - a **receiving thread** that blocks on `receive()` and prints incoming messages as they arrive;
  - the **main thread** that reads the keyboard and sends messages.

  At startup the client sends a "joined the chat" message so the server registers its address immediately and the client can start receiving broadcasts.
- Typing `exit` closes the client's socket.

## 4. Requirements

- JDK 8 or later
- Git

## 5. Build and run

```bash
# Step 2
cd step2-unidirectional
javac *.java
java Server            # terminal 1
java Client            # terminal 2

# Step 3
cd ../step3-broadcast
javac *.java
java Server            # terminal 1
java Client            # terminals 2, 3, 4...
java Client 192.168.1.10   # optional: IP of the machine running the server
```

## 6. Tests

### 6.1 Step 2 — One client

The server was started, then one client. Messages typed in the client appear on the server with the sender's address.

> 📸 *Screenshot 1: server console showing the received messages with IP:port*
> 📸 *Screenshot 2: client terminal with the username and the messages sent*

### 6.2 Step 3 — Several clients

The server was started, then three clients (for example Alice, Bob, Charlie). A message sent by one client is displayed by all the others, but not by the sender.

> 📸 *Screenshot 3: server console showing the registration of new clients and the received messages*
> 📸 *Screenshot 4: the three client terminals side by side, showing the broadcast*

## 7. Synthesis

*(Adjust this section to match what you actually observed.)*

This activity showed how to exchange data in **connectionless mode**. Unlike TCP, UDP has no connection setup: the client simply builds a `DatagramPacket` with the destination address and sends it, and the server receives it together with the sender's address. This is why the server can reply to a client without ever having "accepted" it.

In step 2, the server only displayed what it received. In step 3, since UDP has no notion of connection, the server simulates one by storing the address of every client that sends a message, and uses this list to broadcast each message to the others. On the client side, sending and receiving have to run in separate threads, because `receive()` is blocking and the user can type a message at any moment.

Observations and limits:
- A client only starts receiving messages after sending its first datagram, since that is how the server learns its address.
- UDP gives **no delivery guarantee**: datagrams can be lost, duplicated or reordered. It works well on a local machine but a real chat application would need acknowledgements or TCP.
- The server never removes clients from its list, so after a client leaves, the server keeps sending datagrams to its address. A timeout or a "leave" message could fix this.
- Datagrams are limited by the buffer size (1024 bytes here), so longer messages are truncated.

## 8. Author

Med Hassan Ben Adi — 3rd year Computer Science (GLSI), Faculté des Sciences de Sfax
