namespace Experiment2.Models
{
    public class QuizQuestion
    {
        public string Question { get; set; }
        public List<string> Answers { get; set; }
        public int CorrectAnswerIndex { get; set; }
    }
}
