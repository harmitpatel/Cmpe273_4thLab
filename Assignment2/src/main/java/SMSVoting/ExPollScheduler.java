package SMSVoting;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import beans.Poll;

@Component
public class ExPollScheduler
{

	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	SimpleProducer producer;
	@Autowired
	SMSUtil utility;
	Poll poll;

    @Scheduled(fixedRate = 300000) //5 minutes 300000 miliseconds
    public void mailScheduler() 
    {
    	
    	ArrayList<Poll> expired_Poll = new ArrayList<Poll>();
        producer= new SimpleProducer();
        expired_Poll = utility.pollLookUp();  
        System.out.println("List of Expired Polls: "+expired_Poll);
        
        if(!expired_Poll.isEmpty())
        {
        	for(int i=0; i<expired_Poll.size();i++)
        	{	
        		if(expired_Poll.get(i).isFlag() != true)
				{
        			
        			producer.kafka("cmpe273-topic", expired_Poll.get(i).getModerator().getEmail() + ":010125505:001234567:Poll Result [Android=100,iPhone=200]");
        			System.out.println("Condition Satified!!!");
        			poll = expired_Poll.get(i);
        			poll.setFlag(true);
        			utility.UpdatePoll(poll);
        			System.out.println();
					
				}
        	}	
        }
    }  
}

