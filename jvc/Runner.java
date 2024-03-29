package jvc;

public class Runner {

    public static void run() {

        var events=FileParser.getEvents(); //get declaration lists for signals/event
        var signals=FileParser.getSignals();
        for (var e: events) {

            e.operation(); //execute operation for current event
            System.out.println("Time: "+e.getTime());
            for (var t: signals) System.out.println(t); //dump signal list after each event
            System.out.println();
        }
    }
}
