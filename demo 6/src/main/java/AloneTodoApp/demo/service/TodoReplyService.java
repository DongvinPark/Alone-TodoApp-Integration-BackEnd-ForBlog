package AloneTodoApp.demo.service;

import AloneTodoApp.demo.Exception.AloneTodoAppException;
import AloneTodoApp.demo.model.TodoReplyEntity;
import AloneTodoApp.demo.persistence.TodoReplyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static AloneTodoApp.demo.Exception.AloneTodoErrorCode.NO_TITLE_ENTERED;

@Slf4j
@Service
public class TodoReplyService {

    @Autowired
    private TodoReplyRepository replyRepository;




    @Transactional(readOnly = true)
    public List<TodoReplyEntity> retrieveReplies(String userId){
        log.info("TodoReply 리포지토리 called");
        return replyRepository.findByUserId(userId);
    }




    @Transactional
    public void createReply(String tempUserId, String todoId, String title) {

        log.info("TodoReplyService.createReply method enter");

        /*if(title.equals("") || title == null){
            log.warn("no_title_entered");
            throw new AloneTodoAppException(NO_TITLE_ENTERED);
        }*/

        TodoReplyEntity replyEntity = new TodoReplyEntity(null, tempUserId, todoId, title);

        replyRepository.save(replyEntity);
        log.info("리플라이 리포지토리 worked");
    }//func




    @Transactional
    public List<TodoReplyEntity> updateReply(TodoReplyEntity replyEntity){

        log.info("TodoReplyService.updateReply method entered");

        validateEmptyTodoReplyTile(replyEntity);

        log.info("replyEntity id : ");
        log.info(replyEntity.getId());

        Optional<TodoReplyEntity> original = replyRepository.findById(replyEntity.getId());

        if(original.isPresent()){
            log.info("updateReply method save part entered");
            TodoReplyEntity newTodoReplyEntity = original.get();
            newTodoReplyEntity.setTitle(replyEntity.getTitle());
            replyRepository.save(newTodoReplyEntity);
        }

        return retrieveReplies(replyEntity.getUserId());
    }//func




    public List<TodoReplyEntity> deleteReply(TodoReplyEntity replyEntity) {
        try{
                replyRepository.delete(replyEntity);
        }
        catch(Exception e){
            log.error("error deleting Todo Reply Entity", replyEntity.getId(), e);
            throw new RuntimeException("error deleting entity" + replyEntity.getId());
        }

        return retrieveReplies(replyEntity.getUserId());
    }//func



    //************>>>>>>>>> HELPER METHOD AREA <<<<<<<<<<<************




    private void validateEmptyTodoReplyTile(TodoReplyEntity replyEntity){
        if(replyEntity.getTitle().equals("") || replyEntity.getTitle() == null){
            throw new AloneTodoAppException(NO_TITLE_ENTERED);
        }
    }//func



}//end of class
