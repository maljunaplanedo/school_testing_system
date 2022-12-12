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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
class LiveGapsRequest {
    private int index;
    private char letter;
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
class LiveGapsResponse {
    private boolean ok;
    private boolean full;
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
class LiveGapsGap {
    private String value = "";
    private boolean full = false;
}

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
class LiveGapsAnswer {
    private List<LiveGapsGap> gaps;
    private int filled = 0;
}

@TaskChecker(TaskType.LIVE_GAPS)
public class LiveGapsChecker implements ITaskChecker {
    private static final double FULL_WORD_PRIZE = 10;
    private static final double WRONG_LETTER_PENALTY = 3;

    @Override
    public double computeMaxMark(Task task) throws BadDataFormatException {
        var mapper = new ObjectMapper();
        try {
            return FULL_WORD_PRIZE *
                mapper.readValue(task.getAnswer(), LiveGapsAnswer.class).getGaps().size();
        } catch (IOException exception) {
            throw new BadDataFormatException("Bad data format");
        }
    }

    @Override
    public TaskCheckResult handle(StudentTask task, String requestString) throws BadDataFormatException {
        try {
            var mapper = new ObjectMapper();
            var request = mapper.readValue(requestString, LiveGapsRequest.class);
            var correctAnswer =
                mapper.readValue(task.getClassTask().getTask().getAnswer(), LiveGapsAnswer.class);

            var studentsAnswerString = task.getCurrentAnswer();
            LiveGapsAnswer studentsAnswer;
            if (studentsAnswerString == null) {
                studentsAnswer = new LiveGapsAnswer();
                studentsAnswer.setGaps(
                    Stream
                        .generate(LiveGapsGap::new)
                        .limit(correctAnswer.getGaps().size())
                        .collect(Collectors.toList())
                );
            } else {
                studentsAnswer = mapper.readValue(studentsAnswerString, LiveGapsAnswer.class);
            }

            var mark = task.getCurrentMark();

            var correctGap = correctAnswer.getGaps().get(request.getIndex());
            var studentsGap = studentsAnswer.getGaps().get(request.getIndex());

            if (studentsGap.isFull()) {
                throw new BadDataFormatException("Word is already full");
            }

            var response = new LiveGapsResponse();

            var indexInWord = studentsGap.getValue().length();
            if (request.getLetter() != correctGap.getValue().charAt(indexInWord)) {
                mark -= WRONG_LETTER_PENALTY;
                response.setOk(false);
            } else {
                studentsGap.setValue(studentsGap.getValue().concat(String.valueOf(request.getLetter())));
                response.setOk(true);

                if (studentsGap.getValue().length() == correctGap.getValue().length()) {
                    studentsGap.setFull(true);
                    response.setFull(true);
                    mark += FULL_WORD_PRIZE;
                    studentsAnswer.setFilled(studentsAnswer.getFilled() + 1);
                }

                studentsAnswer.getGaps().set(request.getIndex(), studentsGap);
            }

            task.setCurrentAnswer(mapper.writeValueAsString(studentsAnswer));
            task.setCurrentMark(mark);

            return new TaskCheckResult(
                mapper.writeValueAsString(response),
                studentsAnswer.getFilled() == studentsAnswer.getGaps().size()
            );

        } catch (IOException | IndexOutOfBoundsException exception) {
            throw new BadDataFormatException("Bad data format");
        }
    }
}
