function x = zerocrossings(v)
    x = length(find(diff(v>0)~=0)+1);
end