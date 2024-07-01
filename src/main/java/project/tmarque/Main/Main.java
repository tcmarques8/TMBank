package project.tmarque.Main;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import project.tmarque.TMBank.*;

public class Main {
	private static Collection<Client> clients = new ArrayList<>();
	private static Collection<Account> accounts = new ArrayList<>();
	private static Hashtable<Integer,Account> hashtable = new Hashtable<>();
	private static ArrayList<Flow> flows = new ArrayList<>();
	
	public static void main(String[] args) {
		populateClients();
		ArrayList<Client> list = getClients((ArrayList<Client>) clients);
		System.out.println("Showing Clients");
		showClients(list);
		
		accounts = populateAccounts((ArrayList<Client>)clients);
		
		System.out.println("\n\nShowing Flows");
		addFlows();
		Path json = Path.of("src/main/java/flows.json");
		loadFlowsFromJson(json,(ArrayList<Flow>)flows);
		
		for (Flow f : flows) {
			System.out.println(f);
		}
		
		System.out.println("\n\nShowing Accounts");
		Path xml = Path.of("src/main/java/accounts.xml");
		loadAccountsFromXml(xml, (ArrayList<Account>) accounts);
		getAccounts((ArrayList<Account>)accounts);

		hashtable = accountsTable((ArrayList<Account>)accounts);
		updateBalances(flows,hashtable);
		
		System.out.println("\n\nShowing Accounts in ascending order by balance");
		sortHashtable(hashtable);
	}

	public static ArrayList<Client> getClients(ArrayList<Client> clients) {
		Scanner reader = new Scanner(System.in);
		System.out.println("How many clients do you want to see?\n");
		int num = reader.nextInt();
		ArrayList<Client> newClients = new ArrayList<>();
		for (int i = 0; i < num; i++) {
			newClients.add(clients.get(i));
		}
		reader.close();
		return newClients;
	}
	
	public static void showClients(ArrayList<Client> clients) {
		clients.stream()
			.forEach(client -> System.out.println(client));
	}

	public static void populateClients() {
		clients.add(new Client("Artur", "Vidal"));
		clients.add(new Client("Bernardo", "Franco"));
		clients.add(new Client("Tomé", "Amaral"));
		clients.add(new Client("Marco", "Gomes"));
		clients.add(new Client("Luís", "Mendes"));
		clients.add(new Client("Bruno", "Rocha"));
		clients.add(new Client("João", "Ferreira"));
		clients.add(new Client("Pedro", "Porro"));
		clients.add(new Client("Carlos", "Andrade"));
		clients.add(new Client("Mário", "Barros"));
		clients.add(new Client("Miguel", "Nunes"));
	}
	
	public static ArrayList<Account> populateAccounts(ArrayList<Client> clients) {
		ArrayList<Account> newAccounts = new ArrayList<>();
		for (Client c : clients) {
			Account curAcc = new CurrentAccount("Current Account", c);
			Account savAcc = new SavingsAccount("Savings Account", c);
			newAccounts.add(curAcc);
			newAccounts.add(savAcc);
		}
		return newAccounts;
	}
	
	public static void getAccounts(ArrayList<Account> accounts) {
		accounts.stream()
			.forEach(acc -> System.out.println(acc));
	}
	
	public static Hashtable<Integer,Account> accountsTable(ArrayList<Account> accounts) {
		Hashtable<Integer,Account> table = new Hashtable<>();
		for (Account acc : accounts) {
			table.put(acc.getAccountNumber(), acc);
		}
		return table;
	}
	
	public static void sortHashtable(Hashtable<Integer,Account> accountTable) {
		 List<Map.Entry<Integer, Account>> sortedEntries = accountTable.entrySet()
	                .stream()
	                .sorted((entry1, entry2) -> Double.compare(entry1.getValue().getBalance(), entry2.getValue().getBalance()))
	                .collect(Collectors.toList());

	        sortedEntries.forEach(entry -> System.out.println(entry.getValue()));
	}
	
	public static void addFlows() {
		flows.add(new Debit("Debit", 50, 1));
		for (Account acc : accounts) {
			flows.add(new Credit("Credit",100.5,acc.getAccountNumber()));
		}
		for (Account acc : accounts) {
			if (acc instanceof SavingsAccount) {
				flows.add(new Credit("Credit",1500,acc.getAccountNumber()));
			}
		}
		flows.add(new Transfer("Transfer", 50,2,1));
	}
	
	public static void updateBalances(ArrayList<Flow> flows, Hashtable<Integer,Account> table) {
		for (Flow flow : flows) {
            if (flow instanceof Transfer) {
                Transfer transfer = (Transfer) flow;
                Account sourceAccount = table.get(transfer.getAccountNumber());
                Account targetAccount = table.get(transfer.getTargetAccountnumber());
                if (sourceAccount != null) {
                    sourceAccount.setBalance(flow);
                }
                if (targetAccount != null) {
                    targetAccount.setBalance(flow);
                }
            } else {
                Account targetAccount = table.get(flow.getTargetAccountnumber());
                if (targetAccount != null) {
                    targetAccount.setBalance(flow);
                }
            }
        }
		
		Predicate<Account> hasNegativeBalance = account -> account.getBalance() < 0;
        boolean hasNegative = table.values().stream().anyMatch(hasNegativeBalance);

        if (hasNegative) {
            System.out.println("Warning: There is at least one account with a negative balance.");
        }
	}
	
	public static void loadFlowsFromJson(Path filePath, ArrayList<Flow> flows) {
        
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            StringBuilder jsonContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonContent.append(line);
            }

            JSONArray jsonArray = new JSONArray(jsonContent.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String comment = jsonObject.getString("comment");
                double amount = jsonObject.getDouble("amount");
                int targetAccountNumber = jsonObject.getInt("targetAccountNumber");
                boolean effect = jsonObject.getBoolean("effect");
                String date = jsonObject.getString("date");
                String type = jsonObject.getString("type");

                Flow flow;
                switch (type) {
                    case "Credit":
                        flow = new Credit(comment, amount, targetAccountNumber, effect, date);
                        break;
                    case "Debit":
                        flow = new Debit(comment, amount, targetAccountNumber, effect, date);
                        break;
                    case "Transfer":
                        int accountNumber = jsonObject.getInt("accountNumber");
                        flow = new Transfer(comment, amount, targetAccountNumber, effect, date, accountNumber);
                        break;
                    default:
                        throw new IllegalArgumentException("Unknown flow type: " + type);
                }

                flows.add(flow);
            }	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void loadAccountsFromXml(Path filePath, ArrayList<Account> accounts) {
        
        try (InputStream stream = Files.newInputStream(filePath)) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("Account");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;

                    String label = eElement.getElementsByTagName("label").item(0).getTextContent();
                    double balance = Double.parseDouble(eElement.getElementsByTagName("balance").item(0).getTextContent());
                    int accountNumber = Integer.parseInt(eElement.getElementsByTagName("accountNumber").item(0).getTextContent());

                    Element clientElement = (Element) eElement.getElementsByTagName("client").item(0);
                    String firstname = clientElement.getElementsByTagName("firstname").item(0).getTextContent();
                    String lastname = clientElement.getElementsByTagName("lastname").item(0).getTextContent();

                    Client client = new Client(firstname, lastname);

                    String type = eElement.getAttribute("xsi:type");
                    Account account;
                    if ("CurrentAccount".equals(type)) {
                        account = new CurrentAccount(label, balance, accountNumber, client);
                    } else if ("SavingAccount".equals(type)) {
                        account = new SavingsAccount(label, balance, accountNumber, client);
                    } else {
                        throw new IllegalArgumentException("Unknown account type: " + type);
                    }

                    accounts.add(account);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
