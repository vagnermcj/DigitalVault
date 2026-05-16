CREATE TABLE IF NOT EXISTS Grupos (
    gid INTEGER PRIMARY KEY,
    nome VARCHAR(30) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS Usuarios (
    uid INTEGER PRIMARY KEY AUTOINCREMENT,
    login VARCHAR(255) UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    gid INTEGER NOT NULL,

    senha_hash CHAR(60) NOT NULL,

    totp_secret_encrypted TEXT NOT NULL,

    kid INTEGER NOT NULL,

    bloqueado_ate TIMESTAMP,
    erros_senha INTEGER DEFAULT 0,
    erros_totp INTEGER DEFAULT 0,
    total_acessos INTEGER DEFAULT 0,

    FOREIGN KEY(gid) REFERENCES Grupos(gid),
    FOREIGN KEY(kid) REFERENCES Chaveiro(kid)
);

CREATE TABLE IF NOT EXISTS Chaveiro (
    kid INTEGER PRIMARY KEY AUTOINCREMENT,
    uid INTEGER NOT NULL,

    private_key_encrypted BLOB NOT NULL,
    certificate_pem TEXT NOT NULL,

    FOREIGN KEY(uid) REFERENCES Usuarios(uid)
);

CREATE TABLE IF NOT EXISTS Mensagens (
    mid INTEGER PRIMARY KEY,
    mensagem TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Registros (
    rid INTEGER PRIMARY KEY AUTOINCREMENT,

    data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    mid INTEGER NOT NULL,
    uid INTEGER,
    arquivo_nome VARCHAR(255),

    FOREIGN KEY(mid) REFERENCES Mensagens(mid),
    FOREIGN KEY(uid) REFERENCES Usuarios(uid)
);