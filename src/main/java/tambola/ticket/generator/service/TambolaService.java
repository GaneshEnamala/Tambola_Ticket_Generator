package tambola.ticket.generator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tambola.ticket.generator.model.Ticket;
import tambola.ticket.generator.repositery.TicketRepositery;

@Service
public class TambolaService {

	@Autowired
	TicketRepositery ticketRepositery;

	private static int Number_Tickets_Per_Set = 6;

	public JSONObject generateTickets(int n) {
		
		JSONObject jsonObject = new JSONObject();

		for (int i = 0; i < n; i++) {

			List<Ticket> set = generateSet(n);
			jsonObject = getJsonObject(set);
		}
		
		return jsonObject;

	}

	private JSONObject getJsonObject(List<Ticket> set) {

		JSONObject keyTicketJ = new JSONObject();

		for (Ticket ticket : set) {
			keyTicketJ.put(ticket.getId() + "", ticket.getNumbers());
		}

		JSONObject jsonObj = new JSONObject();

		jsonObj.put("tickets", keyTicketJ);

		return jsonObj;
	}

	private List<Ticket> generateSet(int n) {

		List<Ticket> ticketsList = new ArrayList<>();

		for (int k = 0; k < n; k++) {

			Ticket[] tickets = new Ticket[Number_Tickets_Per_Set];

			for (int i = 0; i < Number_Tickets_Per_Set; i++) {
				tickets[i] = new Ticket();
			}

			List<List<Integer>> columns = new ArrayList<List<Integer>>();

			List<Integer> l1 = new ArrayList<Integer>();
			for (int i = 1; i <= 9; i++) {
				l1.add(i);
			}
			columns.add(l1);

			List<Integer> l2 = new ArrayList<Integer>();
			for (int i = 10; i <= 19; i++) {
				l2.add(i);
			}
			columns.add(l2);

			List<Integer> l3 = new ArrayList<Integer>();
			for (int i = 20; i <= 29; i++) {
				l3.add(i);
			}
			columns.add(l3);

			List<Integer> l4 = new ArrayList<Integer>();
			for (int i = 30; i <= 39; i++) {
				l4.add(i);
			}
			columns.add(l4);

			List<Integer> l5 = new ArrayList<Integer>();
			for (int i = 40; i <= 49; i++) {
				l5.add(i);
			}
			columns.add(l5);

			List<Integer> l6 = new ArrayList<Integer>();
			for (int i = 50; i <= 59; i++) {
				l6.add(i);
			}
			columns.add(l6);

			List<Integer> l7 = new ArrayList<Integer>();
			for (int i = 60; i <= 69; i++) {
				l7.add(i);
			}
			columns.add(l7);

			List<Integer> l8 = new ArrayList<Integer>();
			for (int i = 70; i <= 79; i++) {
				l8.add(i);
			}
			columns.add(l8);

			List<Integer> l9 = new ArrayList<Integer>();
			for (int i = 80; i <= 90; i++) {
				l9.add(i);
			}
			columns.add(l9);

			List<List<Integer>> set1 = new ArrayList<List<Integer>>();
			List<List<Integer>> set2 = new ArrayList<List<Integer>>();
			List<List<Integer>> set3 = new ArrayList<List<Integer>>();
			List<List<Integer>> set4 = new ArrayList<List<Integer>>();
			List<List<Integer>> set5 = new ArrayList<List<Integer>>();
			List<List<Integer>> set6 = new ArrayList<List<Integer>>();

			for (int i = 0; i < 9; i++) {
				set1.add(new ArrayList<Integer>());
				set2.add(new ArrayList<Integer>());
				set3.add(new ArrayList<Integer>());
				set4.add(new ArrayList<Integer>());
				set5.add(new ArrayList<Integer>());
				set6.add(new ArrayList<Integer>());
			}

			List<List<List<Integer>>> sets = new ArrayList<List<List<Integer>>>();

			sets.add(set1);
			sets.add(set2);
			sets.add(set3);
			sets.add(set4);
			sets.add(set5);
			sets.add(set6);

			// assigning elements to each set for each column
			for (int i = 0; i < 9; i++) {
				List<Integer> li = columns.get(i);
				for (int j = 0; j < 6; j++) {
					int randNumIndex = getRand(0, li.size() - 1);
					int randNum = li.get(randNumIndex);

					List<Integer> set = sets.get(j).get(i);
					set.add(randNum);

					li.remove(randNumIndex);
				}
			}

			// assign element from last column to random set
			List<Integer> lastCol = columns.get(8);
			int randNumIndex = getRand(0, lastCol.size() - 1);
			int randNum = lastCol.get(randNumIndex);

			int randSetIndex = getRand(0, sets.size() - 1);
			List<Integer> randSet = sets.get(randSetIndex).get(8);
			randSet.add(randNum);

			lastCol.remove(randNumIndex);

			// 3 passes over the remaining columns
			for (int pass = 0; pass < 3; pass++) {
				for (int i = 0; i < 9; i++) {
					List<Integer> col = columns.get(i);
					if (col.size() == 0)
						continue;

					int randNumIndex_p = getRand(0, col.size() - 1);
					int randNum_p = col.get(randNumIndex_p);

					boolean vacantSetFound = false;
					while (!vacantSetFound) {
						int randSetIndex_p = getRand(0, sets.size() - 1);
						List<List<Integer>> randSet_p = sets.get(randSetIndex_p);

						if (getNumberOfElementsInSet(randSet_p) == 15 || randSet_p.get(i).size() == 2)
							continue;

						vacantSetFound = true;
						randSet_p.get(i).add(randNum_p);

						col.remove(randNumIndex_p);
					}
				}
			}

			// one more pass over the remaining columns
			for (int i = 0; i < 9; i++) {
				List<Integer> col = columns.get(i);
				if (col.size() == 0)
					continue;

				int randNumIndex_p = getRand(0, col.size() - 1);
				int randNum_p = col.get(randNumIndex_p);

				boolean vacantSetFound = false;
				while (!vacantSetFound) {
					int randSetIndex_p = getRand(0, sets.size() - 1);
					List<List<Integer>> randSet_p = sets.get(randSetIndex_p);

					if (getNumberOfElementsInSet(randSet_p) == 15 || randSet_p.get(i).size() == 3)
						continue;

					vacantSetFound = true;
					randSet_p.get(i).add(randNum_p);

					col.remove(randNumIndex_p);
				}
			}

			// sort the internal sets
			for (int i = 0; i < 6; i++) {
				for (int j = 0; j < 9; j++) {
					Collections.sort(sets.get(i).get(j));
				}
			}

			// got the sets - need to arrange in tickets now
			for (int setIndex = 0; setIndex < 6; setIndex++) {
				List<List<Integer>> currSet = sets.get(setIndex);
				Ticket currTicket = tickets[setIndex];

				// fill first row
				for (int size = 3; size > 0; size--) {
					if (currTicket.getRowCount(0) == 5)
						break;
					for (int colIndex = 0; colIndex < 9; colIndex++) {
						if (currTicket.getRowCount(0) == 5)
							break;
						if (currTicket.numbers[0][colIndex] != 0)
							continue;

						List<Integer> currSetCol = currSet.get(colIndex);
						if (currSetCol.size() != size)
							continue;

						currTicket.numbers[0][colIndex] = currSetCol.remove(0);
					}
				}

				// fill second row
				for (int size = 2; size > 0; size--) {
					if (currTicket.getRowCount(1) == 5)
						break;
					for (int colIndex = 0; colIndex < 9; colIndex++) {
						if (currTicket.getRowCount(1) == 5)
							break;
						if (currTicket.numbers[1][colIndex] != 0)
							continue;

						List<Integer> currSetCol = currSet.get(colIndex);
						if (currSetCol.size() != size)
							continue;

						currTicket.numbers[1][colIndex] = currSetCol.remove(0);
					}
				}

				// fill third row
				for (int size = 1; size > 0; size--) {
					if (currTicket.getRowCount(2) == 5)
						break;
					for (int colIndex = 0; colIndex < 9; colIndex++) {
						if (currTicket.getRowCount(2) == 5)
							break;
						if (currTicket.numbers[2][colIndex] != 0)
							continue;

						List<Integer> currSetCol = currSet.get(colIndex);
						if (currSetCol.size() != size)
							continue;

						currTicket.numbers[2][colIndex] = currSetCol.remove(0);
					}
				}
			}

			try {
				for (int i = 0; i < 6; i++) {
					Ticket currTicket = tickets[i];
					currTicket.sortColumns();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			for (int i = 0; i < 6; i++) {
				Ticket currTicket = tickets[i];
				List<List<Integer>> ticket = new ArrayList<>();
				for (int r = 0; r < 3; r++) {
					List<Integer> numbers = new ArrayList<>();
					for (int col = 0; col < 9; col++) {
						int num = currTicket.numbers[r][col];
						if (num != 0) {
							numbers.add(num);
						} else {
							numbers.add(0);
						}

					}
					ticket.add(numbers);
				}
				Ticket t = saveTicket(ticket);
				ticketsList.add(t);
			}

		}
		return ticketsList;
	}

	private Ticket saveTicket(List<List<Integer>> ticket) {
		int[][] numbers = listToArray(ticket);

		Ticket t = new Ticket();
		t.setNumbers(numbers);

		t = ticketRepositery.save(t);

		return t;
	}

	public int getRand(int min, int max) {
		Random rand = new Random();
		return rand.nextInt(max - min + 1) + min;
	}

	public int getNumberOfElementsInSet(List<List<Integer>> set) {
		int count = 0;
		for (List<Integer> li : set)
			count += li.size();
		return count;
	}

	private static int[][] listToArray(List<List<Integer>> listOfLists) {
		// Initialize the array with the size of the list
		int[][] array = new int[listOfLists.size()][];

		for (int i = 0; i < listOfLists.size(); i++) {
			// Convert each list to an array of int
			List<Integer> innerList = listOfLists.get(i);
			array[i] = innerList.stream().mapToInt(Integer::intValue).toArray();
		}

		return array;
	}

	public String getAllTickeets() {
		
		List<Ticket> findAll = ticketRepositery.findAll();
		
		List<Ticket> sorted = findAll.stream()
    .sorted(Comparator.comparing(Ticket::getId))
    .collect(Collectors.toList());

		JSONObject jsonObject = new JSONObject();

		jsonObject = getJsonObject(sorted);

		return jsonObject.toString();

	}

}
