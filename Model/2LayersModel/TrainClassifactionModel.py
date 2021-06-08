import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import accuracy_score


df = pd.read_csv('DataDaily - Copy.csv')
df = df.drop(['dt_iso'], axis=1)
df = df.dropna(axis=0)
train_data = df.head(13984)
val_data = df.tail(1461)

X = train_data.drop(['rain',
                     'temp',
                     'humidity'
               ],
               axis=1).copy()
y = train_data[['rain', 'dummy']].copy()
X_train = X.head(10488)
X_test = X.tail(3496)
y_train = y.head(10488)
y_test = y.tail(3496)

cf_train_features = X_train.drop(['dummy'], axis=1).copy()
cf_train_label = y_train['dummy'].copy()
cf_test_features = X_test.drop(['dummy'], axis=1).copy()
cf_test_label = y_test['dummy'].copy()

classifier = RandomForestClassifier(random_state=123)
classifier.fit(cf_train_features, cf_train_label)
cf_pred = classifier.predict(cf_test_features)
print("Accuracy:", accuracy_score(cf_test_label, cf_pred))

param_grid ={
    'bootstrap': [True, False],
    'max_depth': [4, 5, 6],
    'max_features': [1, 2, 3],
    'min_samples_leaf': [3, 4, 5],
    'min_samples_split': [2, 3, 4],
    'n_estimators': [104, 105, 106]
}
optimal = GridSearchCV(
    estimator=RandomForestClassifier(random_state=123),
    param_grid=param_grid,
    verbose=2,
    n_jobs=10,
    cv=5
)
optimal.fit(cf_train_features,
            cf_train_label)
print(optimal.best_params_)

optimal_classifier = RandomForestClassifier(n_estimators=105,
                                            bootstrap = True,
                                            max_depth = 5,
                                            max_features = 2,
                                            min_samples_leaf = 4,
                                            min_samples_split = 2,
                                            random_state=123)
optimal_classifier.fit(cf_train_features, cf_train_label)
cf_pred = optimal_classifier.predict(cf_test_features)
print("Accuracy:", accuracy_score(cf_test_label, cf_pred))

