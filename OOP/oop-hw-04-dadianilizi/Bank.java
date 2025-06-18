// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StreamTokenizer;

public class Bank {
	public static final int ACCOUNTS = 20;

	private Account[] accounts;
	private final Buffer buffer;

	public Bank() {
		buffer = new Buffer();
		initializeAccounts();
	}


	public void readFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StreamTokenizer tokenizer = new StreamTokenizer(reader);

			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) {
					break;
				}

				int from = (int) tokenizer.nval;
				tokenizer.nextToken();
				int to = (int) tokenizer.nval;

				tokenizer.nextToken();
				int amount = (int) tokenizer.nval;
				buffer.add(new Transaction(from, to, amount));
			}
		} catch (Exception ignored) { }
	}


	public void processFile(String file, int numWorkers) {
		// fork off the workers
		Thread[] workers = new Thread[numWorkers];
		for (int i = 0; i < numWorkers; i++) {
			workers[i] = new Thread(() -> {
				Transaction t;
				while ((t = buffer.remove()) != null) {
					accounts[t.from].change(-t.amount);
					accounts[t.to].change(t.amount);
				}
			});
			workers[i].start();
		}

		readFile(file);
		for (int i = 0; i < numWorkers; i++) {
			buffer.add(null);
		}

		try {
			for (int i = 0; i < numWorkers; i++) {
				workers[i].join();
			}
		} catch (Exception ignored) { }
	}

	public void summary() {
		for (Account account : accounts) {
			System.out.println(account.toString());
		}
	}


	public static void main(String[] args) {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			return;
		}

		String file = args[0];

		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}

		Bank bank = new Bank();
		bank.processFile(file, numWorkers);
		bank.summary();

		System.out.println("All Done");
	}

	private void initializeAccounts() {
		accounts = new Account[ACCOUNTS];
		for (int i = 0; i < ACCOUNTS; i++) {
			accounts[i] = new Account(i);
		}
	}

}