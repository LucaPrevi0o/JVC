package jvc;

public class Runner {

    public static void run() {

        var events=FileParser.getEvents();
        var signals=FileParser.getSignals();
        System.out.println("Starting run operation");
        for (var e: events) {

            e.operation();
            System.out.println("Event @"+e.getTime()+":");
            System.out.println(e.getTarget());
        }

        System.out.println();
        for (var s: signals) System.out.println(s);
    }
}
