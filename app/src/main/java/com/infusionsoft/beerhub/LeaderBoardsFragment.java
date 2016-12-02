package com.infusionsoft.beerhub;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.infusionsoft.beerhub.databinding.LeaderboardListItemBinding;
import com.infusionsoft.beerhub.databinding.LeaderboardsLayoutBinding;
import com.infusionsoft.beerhub.model.BeerDrinker;
import com.infusionsoft.beerhub.model.BeerDrinkerFields;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import java.util.List;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * File description here...
 */
public class LeaderBoardsFragment extends Fragment {

    private static final String TAG = LeaderBoardsFragment.class.getSimpleName();

    private List<BeerDrinker> drinkers;
    private LeaderboardsLayoutBinding binding;
    private BeerDrinkerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.leaderboards_layout, container, false);

        binding.recycler.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.recycler.setLayoutManager(layoutManager);
        binding.recycler.setAdapter(adapter = new BeerDrinkerAdapter());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterBy(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
    }

    private void filterBy(int index) {
        String field = BeerDrinkerFields.NET_BEERS;
        Sort sort = index == 0 ? Sort.DESCENDING : Sort.ASCENDING;
        final Realm realm = Realm.getDefaultInstance();
        realm.where(BeerDrinker.class)
            .findAllSorted(field, sort)
            .<List<BeerDrinker>>asObservable()
            .map(new Func1<RealmResults<BeerDrinker>, List<BeerDrinker>>() {
                @Override
                public List<BeerDrinker> call(RealmResults<BeerDrinker> beerDrinkers) {
                    return realm.copyFromRealm(beerDrinkers);
                }
            })
            .subscribe(new Action1<List<BeerDrinker>>() {
                @Override
                public void call(List<BeerDrinker> beerDrinkers) {
                    drinkers = beerDrinkers.size() > 10 ? beerDrinkers.subList(0,10) : beerDrinkers;
                    adapter.notifyDataSetChanged();
                }
            });
    }

    public class BeerDrinkerViewHolder extends RecyclerView.ViewHolder {

        private BeerDrinkerViewModel viewModel;


        public BeerDrinkerViewHolder(View itemView, BeerDrinkerViewModel viewModel) {
            super(itemView);
            this.viewModel = viewModel;
        }

        public void bind(BeerDrinker drinker) {
            viewModel.setDrinker(drinker);
        }
    }

    private class BeerDrinkerAdapter extends RecyclerView.Adapter<BeerDrinkerViewHolder> {

        private final LayoutInflater layoutInflater;

        public BeerDrinkerAdapter() {
            layoutInflater = getActivity().getLayoutInflater();
        }

        @Override
        public BeerDrinkerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LeaderboardListItemBinding binding = DataBindingUtil.inflate(
                layoutInflater,
                R.layout.leaderboard_list_item, parent, false);
            BeerDrinkerViewModel viewModel = new BeerDrinkerViewModel();
            binding.setViewModel(viewModel);

            BeerDrinkerViewHolder viewHolder = new BeerDrinkerViewHolder(binding.getRoot(),
                viewModel);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BeerDrinkerViewHolder holder, int position) {
            holder.bind(drinkers.get(position));
        }

        @Override
        public int getItemCount() {
            return drinkers == null ? 0 : drinkers.size();
        }
    }
}
