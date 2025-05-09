# App de Conversas Instantâneas com Sockets (Java)

Este projeto é um sistema de mensagens instantâneas em Java, utilizando sockets TCP para comunicação entre clientes através de um servidor central. O aplicativo é composto por dois programas: um servidor responsável por gerenciar as conexões e rotear as mensagens, e um cliente que permite o envio e recebimento de mensagens e arquivos.

---

## Funcionalidades

- Comunicação entre clientes por meio de um servidor intermediário;
- Envio de mensagens privadas entre dois clientes;
- Envio e recebimento de arquivos;
- Listagem de usuários conectados com `/users`;
- Encerramento da conexão com `/sair`;
- Registro de conexões (IP e horário) em log pelo servidor.

---

## Arquitetura

- **Servidor**: Gerencia conexões, roteia mensagens e mantém log de conexões.
- **Cliente**: Envia/recebe mensagens e arquivos, e interpreta comandos.

---

### Pré-requisitos

- Java 8 ou superior instalado
- Terminal/Shell para executar os programas
- IDE (opcional) para visualização do código

### Comandos 

#### Enviar mensagem para outro cliente

```bash
/send message <destinatario> <mensagem>
```

#### Enviar arquivo para outro cliente

```bash
/send file <destinatario> <caminho/do/arquivo>
```

#### Listar usuários conectados

```bash
/users
```

#### Sair do chat

```bash
/sair
```

---

## Licença
Este projeto é apenas para fins educacionais e não possui licença comercial.


