package ch.sebooom.dump1090.tcp;

import ch.sebooom.dump1090.messages.sbs1.MessageType;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by seb on .
 * <p>
 * ${VERSION}
 */
public class TCPStatsAggregator {

    private HashMap<MessageType,AtomicInteger> messagesByType = new HashMap<>();
    private AtomicInteger totalCount = new AtomicInteger();
    private String startAggregationDate = null;
    private String stopAggreagtionDate = null;
    private String totalPeriodDuration = null;

    public static TCPStatsAggregator from(List<Map> tcpStatsEntities) {
        return new TCPStatsAggregator(tcpStatsEntities);
    }




    private TCPStatsAggregator(List<Map> messages){


        System.out.println("ok1:");


        try{
            Map rootStart = messages.get(0);
            Map timingStart = (Map)rootStart.get("timing");
            long startMillis = (long) timingStart.get("start_millis");


            Map rootStop = messages.get(messages.size()-1);
            Map timingStop = (Map)rootStop.get("timing");
            long stopMillis = (long) timingStop.get("stop_millis");

            System.out.println("duration: " + (stopMillis - startMillis));


            totalPeriodDuration = getDurationAsHumanReadable(stopMillis - startMillis);
            startAggregationDate = new Date(startMillis).toString();
            stopAggreagtionDate = new Date(stopMillis).toString();

            messages.stream().forEach(entity -> dealEntity(entity));
        }catch (Exception e){
            e.printStackTrace();
        }





    }

    private String getDurationAsHumanReadable(Long duration){

       return String.format("%3d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(duration),
                TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));

    }

    private void dealEntity(Map entity){

        Map msgs = (Map)entity.get("msgs");
        Map<String,Integer> msgByType = (Map<String,Integer>)msgs.get("byType");

        msgByType.keySet().forEach(msgType -> {

            MessageType type = MessageType.valueOf(msgType);
            Integer messageOccurence = msgByType.get(msgType).intValue();

            //add message type as map key if it didnt already exist
            messagesByType.putIfAbsent(type, new AtomicInteger(0));

            //increment msg by type counter
            messagesByType.get(type).addAndGet(messageOccurence);

            totalCount.getAndAdd(messageOccurence);
        });

    }
}
