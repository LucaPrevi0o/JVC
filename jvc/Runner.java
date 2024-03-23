package jvc;

public class Runner {

    public static void run() {

        var events=FileParser.getEvents();
        var signals=FileParser.getSignals();
        System.out.println("\n--- --- ---\n\nStarting run operation\n");
        for (var e: events) {

            e.operation();
            System.out.println("Time: "+e.getTime());
            for (var t: signals) System.out.println(t);
            System.out.println();
        }

        System.out.println("--- --- ---");
    }
}
