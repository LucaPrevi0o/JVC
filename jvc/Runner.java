package jvc;

public class Runner {

    public static void run() {

        var events=FileParser.getEvents();
        var signals=FileParser.getSignals();
        for (var e: events) {

            e.operation();
            System.out.println("Time: "+e.getTime());
            for (var t: signals) System.out.println(t);
            System.out.println();
        }
    }
}
