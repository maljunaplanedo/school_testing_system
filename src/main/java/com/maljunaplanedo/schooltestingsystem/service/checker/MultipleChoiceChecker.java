package com.maljunaplanedo.schooltestingsystem.service.checker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maljunaplanedo.schooltestingsystem.annotation.TaskChecker;
import com.maljunaplanedo.schooltestingsystem.exception.BadDataFormatException;
import com.maljunaplanedo.schooltestingsystem.model.StudentTask;
import com.maljunaplanedo.schooltestingsystem.model.Task;
import com.maljunaplanedo.schooltestingsystem.model.TaskType;
import com.maljunaplanedo.schooltestingsystem.service.ITaskChecker;
import com.maljunaplanedo.schooltestingsystem.service.TaskCheckResult;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
class MultipleChoiceAnswer {
    private List<Integer> answers;
}

@TaskChecker(TaskType.MULTIPLE_CHOICE)
public class MultipleChoiceChecker implements ITaskChecker {
    private static final double CORRECT_ANSWER_PRIZE = 5;

    @Override
    public double computeMaxMark(Task task) throws BadDataFormatException {
        var mapper = new ObjectMapper();
        try {
            return CORRECT_ANSWER_PRIZE *
                mapper.readValue(task.getAnswer(), MultipleChoiceAnswer.class).getAnswers().size();
        } catch (IOException exception) {
            throw new BadDataFormatException("Bad data format");
        }
    }

    @Override
    public TaskCheckResult handle(StudentTask task, String request) throws BadDataFormatException {
        try {
            var mapper = new ObjectMapper();

            var studentsAnswer = mapper.readValue(request, MultipleChoiceAnswer.class);
            var correctAnswer =
                mapper.readValue(task.getClassTask().getTask().getAnswer(), MultipleChoiceAnswer.class);

            double mark = 0;
            for (int i = 0; i < correctAnswer.getAnswers().size(); ++i) {
                if (correctAnswer.getAnswers().get(i).equals(studentsAnswer.getAnswers().get(i))) {
                    mark += CORRECT_ANSWER_PRIZE;
                }
            }

            task.setCurrentAnswer(request);
            task.setCurrentMark(mark);
            return new TaskCheckResult(null, true);
        } catch (IOException | IndexOutOfBoundsException exception) {
            throw new BadDataFormatException("Bad data format");
        }
    }
}
