This project utilizes red and white wine data gotten from UC Irvine's database, along with a rating done by judges on a 0-10 scale, 0 being the worst with 10 being the best. 
That data is then used to train a multinomial logistic regression model that will predict the quality of a wine data entry on that 0-10 scale.
Once the model has done its predictions the accuracy is tested based on micro and macro ROC AUC scores that are then displayed.
‐	Preprocessed and normalized multi-class CSV datasets using quantile transformation techniques
‐	Trained regularized multinomial logistic regression models with an 80/20 train-test split
‐	Evaluated performance using accuracy scores and one-vs-rest ROC AUC with both micro and macro averaging
‐	Employed LabelBinarizer to enable multi-class AUC analysis, providing insights into classification performance across quality levels

