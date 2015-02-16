close all; clc; clear all;

%data_jumps = csvread('20jumpsGuliz.csv');
%data_steps = csvread('100stepsGuliz.csv');
data_jumps = csvread('30jumps.csv');
data_steps = csvread('100stepswturns.csv');

time_jumps=data_jumps(200:end,1)-data_jumps(200,1);
time_steps=data_steps(200:end,1)-data_steps(200,1);

acc_j = sqrt(sum(data_jumps(:,2:4).^2,2));
rot_j = sqrt(sum(data_jumps(:,5:7).^2,2));
mag_j = sqrt(sum(data_jumps(:,8:10).^2,2));
J = [acc_j, rot_j, mag_j];
J = J(200:end, :);

acc_s = sqrt(sum(data_steps(:,2:4).^2,2));
rot_s = sqrt(sum(data_steps(:,5:7).^2,2));
mag_s = sqrt(sum(data_steps(:,8:10).^2,2));
S = [acc_s, rot_s, mag_s];
S = S(200:end, :);

%plot jumps
figure;
subplot(2,1,1); plot(time_jumps, J);
title('jumps');
legend('acc_{total}', 'rotation_{total}', 'mag_{total}');


%plot steps
subplot(2,1,2);plot(time_steps,S);
title('steps');
legend('acc_{trans}', 'rotation_{total}', 'mag_{total}');

figure;
title('Jumps - Rotation');
subplot(3,1,1); plot(data_jumps(:,1),data_jumps(:,5));
subplot(3,1,2); plot(data_jumps(:,1),data_jumps(:,6));
subplot(3,1,3); plot(data_jumps(:,1),data_jumps(:,7));

figure;
title('Steps - Rotation');
subplot(3,1,1); plot(data_steps(:,1),data_steps(:,5));
subplot(3,1,2); plot(data_steps(:,1),data_steps(:,6));
subplot(3,1,3); plot(data_steps(:,1),data_steps(:,7));

%create scatter plot
%split into 5s intervals
time_steps_scattered = floor(time_steps./1000)+1;
time_jumps_scattered = floor(time_jumps./1000)+1;

index_steps = cumsum(histc(time_steps_scattered, unique(time_steps_scattered)));
index_jumps = cumsum(histc(time_jumps_scattered, unique(time_jumps_scattered)));


steps_mean_acc = accumarray(time_steps_scattered,S(:,1), [], @mean);
steps_median_acc = accumarray(time_steps_scattered,S(:,1), [], @median);
steps_max_acc = accumarray(time_steps_scattered,S(:,1), [], @max);
steps_var_acc = accumarray(time_steps_scattered,S(:,1), [], @var);
steps_zerocrossings_acc = accumarray(time_steps_scattered,S(:,1), [], @zerocrossings);

steps_mean_rot = accumarray(time_steps_scattered,S(:,2), [], @mean);
steps_median_rot = accumarray(time_steps_scattered,S(:,2), [], @median);
steps_max_rot = accumarray(time_steps_scattered,S(:,2), [], @max);
steps_var_rot = accumarray(time_steps_scattered,S(:,2), [], @var);
steps_zerocrossings_rot = accumarray(time_steps_scattered,S(:,2), [], @zerocrossings);

steps_mean_mag = accumarray(time_steps_scattered,S(:,3), [], @mean);
steps_median_mag = accumarray(time_steps_scattered,S(:,3), [], @median);
steps_max_mag = accumarray(time_steps_scattered,S(:,3), [], @max);
steps_var_mag = accumarray(time_steps_scattered,S(:,3), [], @var);
steps_zerocrossings_mag = accumarray(time_steps_scattered,S(:,3), [], @zerocrossings);

jumps_mean_acc = accumarray(time_jumps_scattered,J(:,1), [], @mean);
jumps_median_acc = accumarray(time_jumps_scattered,J(:,1), [], @median);
jumps_max_acc = accumarray(time_jumps_scattered,J(:,1), [], @max);
jumps_var_acc = accumarray(time_jumps_scattered,J(:,1), [], @var);
jumps_zerocrossings_acc = accumarray(time_jumps_scattered,J(:,1), [], @zerocrossings);

jumps_mean_rot = accumarray(time_jumps_scattered,J(:,2), [], @mean);
jumps_median_rot = accumarray(time_jumps_scattered,J(:,2), [], @median);
jumps_max_rot = accumarray(time_jumps_scattered,J(:,2), [], @max);
jumps_var_rot = accumarray(time_jumps_scattered,J(:,2), [], @var);
jumps_zerocrossings_rot = accumarray(time_jumps_scattered,J(:,2), [], @zerocrossings);

jumps_mean_mag = accumarray(time_jumps_scattered,J(:,3), [], @mean);
jumps_median_mag = accumarray(time_jumps_scattered,J(:,3), [], @median);
jumps_max_mag = accumarray(time_jumps_scattered,J(:,3), [], @max);
jumps_var_mag = accumarray(time_jumps_scattered,J(:,3), [], @var);
jumps_zerocrossings_mag = accumarray(time_jumps_scattered,J(:,3), [], @zerocrossings);

figure;
scatter3(jumps_var_mag, jumps_max_acc, jumps_var_acc);
hold on;
scatter3(steps_var_mag, steps_max_acc, steps_var_acc);
xlabel('var-magnetometer'); ylabel('max-acceleration'); zlabel('var-acceleration');

