Projeto DAO com JDBC em Java
Este projeto é uma aplicação Java de console que demonstra a implementação do padrão de design DAO (Data Access Object) para interagir com um banco de dados relacional usando JDBC. O sistema gerencia duas entidades principais: Vendedores (Seller) e Departamentos (Department).

🎯 Objetivo
O principal objetivo deste projeto é mostrar uma arquitetura robusta para aplicações Java que precisam de acesso a dados, focando em:

Separação de Responsabilidades: Isolar a lógica de acesso a dados (DAO) da lógica de negócios (que usaria os DAOs).

Baixo Acoplamento: Usar interfaces e uma fábrica (Factory) para que a aplicação não dependa diretamente de uma implementação JDBC específica.

Gerenciamento de Recursos: Centralizar o gerenciamento de conexões e recursos JDBC (como Statement e ResultSet) para evitar vazamentos.

✨ Conceitos e Padrões Utilizados
DAO (Data Access Object): Interfaces (SellerDao, DepartmentDao) definem os contratos para operações de persistência, enquanto classes concretas (SellerDaoJDBC, DepartmentDaoJDBC) implementam esses contratos usando JDBC.

Factory Pattern: A classe DaoFactory é usada para instanciar e fornecer os objetos DAO para a aplicação, ocultando os detalhes da implementação.

Gerenciamento de Conexão: A classe DB centraliza a obtenção e o fechamento da conexão com o banco de dados, lendo as credenciais de um arquivo db.properties.

Tratamento de Exceções: Exceções personalizadas (DbException, DbIntegrityException) são usadas para encapsular SQLExceptions, convertendo exceções checadas (checked) em exceções de tempo de execução (runtime).

Mapeamento Objeto-Relacional (Manual): As implementações do DAO (ex: SellerDaoJDBC) contêm métodos auxiliares (instantiateSeller, instantiateDepartment) para transformar os dados de um ResultSet em objetos de modelo.

📁 Estrutura do Projeto
O projeto está organizado nos seguintes pacotes:

db

DB.java: Classe utilitária para gerenciar a conexão JDBC (abrir e fechar).

DbException.java: Exceção personalizada de tempo de execução para erros de banco de dados.

DbIntegrityException.java: Exceção personalizada para violações de integridade referencial (ex: tentar excluir um departamento que ainda possui vendedores).

model.entities

Department.java: Entidade de modelo que representa um Departamento.

Seller.java: Entidade de modelo que representa um Vendedor (associado a um Department).

model.dao

DepartmentDao.java: Interface que define as operações de CRUD para Department.

SellerDao.java: Interface que define as operações de CRUD para Seller, incluindo buscas por departamento.

DaoFactory.java: Classe responsável por criar e retornar as implementações dos DAOs.

model.dao.impl

DepartmentDaoJDBC.java: Implementação concreta da interface DepartmentDao usando JDBC.

SellerDaoJDBC.java: Implementação concreta da interface SellerDao usando JDBC.

🚀 Como Configurar e Usar
Para executar este projeto, você precisará de um banco de dados (como MySQL ou PostgreSQL) e configurar a conexão.

1. Configuração do Banco de Dados
Você precisa de um banco de dados com duas tabelas: department e seller. Você pode usar os seguintes scripts SQL para criá-las (exemplo para MySQL):

SQL

CREATE TABLE department (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) DEFAULT NULL,
  PRIMARY KEY (Id)
);

CREATE TABLE seller (
  Id int(11) NOT NULL AUTO_INCREMENT,
  Name varchar(60) NOT NULL,
  Email varchar(100) NOT NULL,
  BirthDate datetime NOT NULL,
  BaseSalary double NOT NULL,
  DepartmentId int(11) NOT NULL,
  PRIMARY KEY (Id),
  FOREIGN KEY (DepartmentId) REFERENCES department (Id)
);
2. Arquivo de Propriedades
Na raiz do seu projeto (ou no local de onde o ClassLoader o lê), crie um arquivo chamado db.properties. A classe DB.java usa este arquivo para obter a URL, o usuário e a senha do banco.

Exemplo de db.properties (para MySQL):

Properties

dburl=jdbc:mysql://localhost:3306/meubanco?useSSL=false&serverTimezone=UTC
user=seu_usuario
password=sua_senha
Obs: Altere meubanco, seu_usuario e sua_senha de acordo com a sua configuração local.

3. Executando a Aplicação
Você precisará de uma classe principal (ex: Program.java, que não foi incluída nos arquivos) para interagir com os DAOs.

Exemplo de uso (em uma classe Program.java):

Java

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.Date;
import java.util.List;

public class Program {

    public static void main(String[] args) {

        // Obter o DAO a partir da fábrica
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("=== TEST 1: seller findById ===");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n=== TEST 2: seller findByDepartment ===");
        Department department = new Department(2, null);
        List<Seller> list = sellerDao.findByDepartment(department);
        for (Seller obj : list) {
            System.out.println(obj);
        }

        System.out.println("\n=== TEST 3: seller findAll ===");
        list = sellerDao.findAll();
        for (Seller obj : list) {
            System.out.println(obj);
        }

        System.out.println("\n=== TEST 4: seller insert ===");
        Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
        sellerDao.insert(newSeller);
        System.out.println("Inserted! New id = " + newSeller.getId());

        System.out.println("\n=== TEST 5: seller update ===");
        seller = sellerDao.findById(1);
        seller.setName("Martha Waine");
        sellerDao.update(seller);
        System.out.println("Update completed");

        System.out.println("\n=== TEST 6: seller delete ===");
        sellerDao.deleteId(10); // Coloque um ID que exista
        System.out.println("Delete completed");
    }
}
