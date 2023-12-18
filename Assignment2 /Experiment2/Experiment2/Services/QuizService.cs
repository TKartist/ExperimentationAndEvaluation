using Experiment2.Models;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Threading.Tasks;
using System.Text.Json;

namespace Experiment2.Services
{
    public class QuizService
    {
        private readonly HttpClient _http;
        private List<QuizQuestion> questions;
        private List<QuizResult> results = new List<QuizResult>();
        private ParticipantDetails participantDetails;
        private int currentQuestionIndex = -1;
        private DateTime questionStartTime;

        public QuizService(HttpClient http)
        {
            _http = http;
        }

        public async Task InitializeAsync()
        {
            var response = await _http.GetAsync("./JSONS/questionsJSON.json");
            if (response.IsSuccessStatusCode)
            {
                var jsonString = await response.Content.ReadAsStringAsync();
                questions = JsonSerializer.Deserialize<List<QuizQuestion>>(jsonString) ?? new List<QuizQuestion>();
            }
        }

        public QuizQuestion GetNextQuestion()
        {
            currentQuestionIndex++;
            if (currentQuestionIndex < questions.Count)
            {
                questionStartTime = DateTime.Now;
                return questions[currentQuestionIndex];
            }
            return null;
        }

        public int CalculateScore(int answerIndex)
        {
            var timeTaken = DateTime.Now - questionStartTime;
            var isCorrect = questions[currentQuestionIndex].CorrectAnswerIndex == answerIndex;
            var score = isCorrect ? Math.Max(0, 1000 - (int)timeTaken.TotalSeconds * 10) : 0;

            results.Add(new QuizResult
            {
                Question = questions[currentQuestionIndex].Question,
                IsCorrect = isCorrect,
                Score = score
            });

            return score;
        }

        public void AddParticipantDetails(ParticipantDetails details)
        {
            participantDetails = details;
        }

        public string GetResultsJson()
        {
            var resultsData = new ResultsData
            {
                Questions = results,
                Participant = participantDetails
            };

            return JsonSerializer.Serialize(resultsData, new JsonSerializerOptions { WriteIndented = true });
        }

        public bool HasQuestions()
        {
            return currentQuestionIndex < questions.Count - 1;
        }
    }

    public class ResultsData
    {
        public List<QuizResult> Questions { get; set; }
        public ParticipantDetails Participant { get; set; }
    }
}
