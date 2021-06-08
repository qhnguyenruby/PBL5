import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.model_selection import GridSearchCV
from sklearn import metrics


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

srs_train_features = X.drop(['dummy'], axis=1).copy()
srs_train_label = y['rain'].copy()
srs_test_features = X_test.drop(['dummy'], axis=1).copy()
srs_test_label = y_test['rain'].copy()

regression = RandomForestRegressor(random_state = 123)
regression.fit(srs_train_features, srs_train_label)
srs_pred = regression.predict(srs_test_features)
print('Mean Absolute Error (MAE):', metrics.mean_absolute_error(srs_test_label, srs_pred))
print('Mean Squared Error (MSE):', metrics.mean_squared_error(srs_test_label, srs_pred))

param_grid ={
    'bootstrap': [True, False],
    'max_depth': [8, 9, 10],
    'max_features': [3, 4, 5],
    'min_samples_leaf': [7, 8, 9],
    'min_samples_split': [2, 3, 4],
    'n_estimators': [174, 175, 176]
}

optimal = GridSearchCV(
    estimator=RandomForestRegressor(random_state=123),
    param_grid=param_grid,
    verbose=2,
    n_jobs=10,
    cv=5
)
optimal.fit(srs_train_features,
            srs_train_label)
print(optimal.best_params_)

optimal_regression = RandomForestRegressor(n_estimators=175,
                                            bootstrap = True,
                                            max_depth = 9,
                                            max_features = 4,
                                            min_samples_leaf = 8,
                                            min_samples_split = 2,
                                            random_state=123)
optimal_regression.fit(srs_train_features, srs_train_label)
srs_pred = optimal_regression.predict(srs_test_features)
print('Mean Absolute Error (MAE):', metrics.mean_absolute_error(srs_test_label, srs_pred))
print('Mean Squared Error (MSE):', metrics.mean_squared_error(srs_test_label, srs_pred))